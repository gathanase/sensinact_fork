package org.eclipse.sensinact.gateway.remote.socket.sample;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.sensinact.gateway.common.bundle.ManagedConfigurationListener;
import org.eclipse.sensinact.gateway.common.bundle.Mediator;
import org.eclipse.sensinact.gateway.common.execution.Executable;
import org.eclipse.sensinact.gateway.common.primitive.Name;
import org.eclipse.sensinact.gateway.common.primitive.Nameable;
import org.eclipse.sensinact.gateway.core.Core;
import org.eclipse.sensinact.gateway.remote.socket.SocketEndpoint;

/**
 *
 * @author <a href="mailto:christophe.munilla@cea.fr">Christophe Munilla</a>
 */
public class SocketEndpointManager implements ManagedConfigurationListener
{
	//********************************************************************//
	//						NESTED DECLARATIONS			  			      //
	//********************************************************************//
	
	class SocketEndpointDescriptor implements Nameable
	{
		private final String prefix;
		String remoteAddress = null;
	    int remotePort = -1;
		String localAddress = null;
		int localPort = -1;

		SocketEndpointDescriptor(String prefix)
		{
			this.prefix = prefix;
		}

		@Override
		public String getName()
		{
			return this.prefix;
		}
		
		public boolean complete()
		{
			return (remoteAddress!=null && localAddress!=null
				&& remotePort>0 && localPort >0);
		}
	}
	
	//********************************************************************//
	//						ABSTRACT DECLARATIONS						  //
	//********************************************************************//

	//********************************************************************//
	//						STATIC DECLARATIONS							  //
	//********************************************************************//

	//********************************************************************//
	//						INSTANCE DECLARATIONS						  //
	//********************************************************************//

	private Map<SocketEndpointDescriptor, SocketEndpoint> map;
	private Mediator mediator;

	/**
	 * Constructor
	 * 
	 * @param mediator the {@link Mediator} allowing the SocketEndpointManager 
	 * to be instantiated to interact with the OSGi host environment
	 */
	public SocketEndpointManager(Mediator mediator)
	{
		this.mediator = mediator;
		this.map = new HashMap<>();
	}

	/**
	 * @inheritDoc
	 *
	 * @see org.eclipse.sensinact.gateway.common.bundle.ManagedConfigurationListener#
	 * updated(java.util.Dictionary)
	 */
	@Override
	public void updated(Dictionary<String, ?> properties)
	{		
		synchronized(this.map)
		{
			List<SocketEndpointDescriptor> descriptors = 
					new ArrayList<SocketEndpointDescriptor>();
			
			Enumeration<String> e = properties.keys();
			while(e.hasMoreElements())
			{
				try
				{
				    String key = e.nextElement();
				    String prefix = key.substring(0,key.lastIndexOf('.'));
				    
				    SocketEndpointDescriptor descriptor = null;
				    int index = -1;
				    
				    if((index = descriptors.indexOf(
				    		new Name<SocketEndpointDescriptor>(prefix)))>-1)
				    {
				    	descriptor = descriptors.get(index);
				    	
				    } else
				    {
				    	descriptor =  new SocketEndpointDescriptor(prefix);
				    	descriptors.add(descriptor);
				    }
				    String value = key.substring(prefix.length()+1);
				    switch(value)
				    {
				    	case "remoteAddress":
				    		descriptor.remoteAddress = (String) properties.get(key);
				    	break;
				    	case "remotePort":
				    		descriptor.remotePort = Integer.parseInt((String)
				    				properties.get(key));
				    	break;
				    	case "localAddress":
				    		descriptor.localAddress = (String) properties.get(key);
				    	break;
				    	case "localPort":
				    		descriptor.localPort = Integer.parseInt((String)
				    				properties.get(key));
				    	break;
				    	default:
				    		break;
				    }
				} catch(IndexOutOfBoundsException ex)
				{
					continue;
				}
			}
			Iterator<Entry<SocketEndpointDescriptor, SocketEndpoint>> 
			entryIterator = this.map.entrySet().iterator();
			
			while(entryIterator.hasNext())
			{
				Entry<SocketEndpointDescriptor,SocketEndpoint> entry = 
						entryIterator.next();
				SocketEndpointDescriptor descriptor = entry.getKey();
				
				int index = -1;
				if((index = descriptors.indexOf(new Name<SocketEndpointDescriptor>(
					descriptor.prefix)))==-1 || !descriptors.remove(index).complete())
				{
					entry.getValue().disconnect();
					entryIterator.remove();
				}
			}
			Iterator<SocketEndpointDescriptor> iterator = descriptors.iterator();
			
			while(iterator.hasNext())
			{
				SocketEndpointDescriptor descriptor = iterator.next();
				if(!descriptor.complete())
				{
					continue;
				}
				final SocketEndpoint endpoint = createEndpoint(descriptor);
				if(endpoint != null)
				{
					this.map.put(descriptor, endpoint);
					new Thread(new Runnable()
					{
						@Override
						public void run()
						{			
							int tries = 5;
							while(endpoint.namespace() == null
									&& tries > 0)
							{
								endpoint.doConnect();
								tries-=1;
								try
								{
									Thread.sleep(150);
									
								} catch (InterruptedException e)
								{
									Thread.interrupted();
									break;
								}
							}
							if(endpoint.namespace()!=null)
							{
								registerEndpoint(endpoint);
							}
						}}
					).start();
				}
			}
		}
	}

	private SocketEndpoint createEndpoint(final SocketEndpointDescriptor descriptor)
	{
		return mediator.callService(Core.class, new Executable<Core,SocketEndpoint>()
		{
			@Override
			public SocketEndpoint execute(final Core core) throws Exception
			{
				final SocketEndpoint endpoint = new SocketEndpoint(
						mediator, core.namespace(),						
						descriptor.localAddress,
						descriptor.localPort,
						descriptor.remoteAddress,
						descriptor.remotePort);	
				return endpoint;
			}
		});
	}

	private void registerEndpoint(final SocketEndpoint endpoint)
	{
		mediator.callService(Core.class, new Executable<Core,Void>()
		{
			@Override
			public Void execute(final Core core) throws Exception
			{
				core.createRemoteCore(endpoint);
				return null;
			}
		});
	}
	
	/**
	 * Stops this SocketEndpointManager and disconnect all the managed
	 * {@link SocketEndpoint}s
	 */
	public void stop()
	{
		synchronized(this.map)
		{
			Iterator<Map.Entry<SocketEndpointDescriptor, SocketEndpoint>> 
			iterator = this.map.entrySet().iterator();
			
			while(iterator.hasNext())
			{
				Entry<SocketEndpointDescriptor, SocketEndpoint> entry =
						iterator.next();
				SocketEndpoint endpoint = entry.getValue();
				endpoint.disconnect();
			}
			this.map.clear();
		}
	}
}
