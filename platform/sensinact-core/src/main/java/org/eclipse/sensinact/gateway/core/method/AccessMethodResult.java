/*
 * Copyright (c) 2017 CEA.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    CEA - initial API and implementation
 */
package org.eclipse.sensinact.gateway.core.method;

import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import org.json.JSONArray;
import org.json.JSONObject;

import org.eclipse.sensinact.gateway.core.message.SnaMessage;
import org.eclipse.sensinact.gateway.common.primitive.PathElement;
import org.eclipse.sensinact.gateway.common.bundle.Mediator;

/**
 * Intermediate {@link AccessMethodExecutor}'s execution result
 * 
 * @param <V>
 * 		result data type
 * 
 * @author <a href="mailto:christophe.munilla@cea.fr">Christophe Munilla</a>
 */
public abstract class AccessMethodResult extends Stack<JSONObject> 
implements PathElement
{	
	/**
	 * Creates and returns an extended {@link AbstractSnaMessage}
	 * whose {@link SnaMessage.Status} is passed as 
	 * parameter
	 * 
	 * @param status
	 * 		the {@link SnaMessage.Status} of the extended 
	 * 		 {@link AbstractSnaMessage} to create
	 * @param accessLevel
	 * 		the integer access level of the extended 
	 * 		 {@link AbstractSnaMessage} to create
	 * @return
	 * 		the created extended {@link AbstractSnaMessage}
	 */
	protected abstract AccessMethodResponse createSnaResponse(
			AccessMethodResponse.Status status);

	/**
	 * Mediator used to interact with the OSGi host
	 * environment 
	 */
	protected final Mediator mediator;
	
	private final Object[] parameters;
	
	private boolean exitOnError;  
	
	private Deque<Exception> exceptions = null;

	private final String uri;

	private JSONObject resultObject;
	
	/**
	 * Constructor
	 * 
	 * @param parameters
	 * 		the parameter objects array of this 
	 * 		AccessMethodResult
	 */
	protected AccessMethodResult(Mediator mediator, 
			String uri,	Object[] parameters)
	{
		this(mediator, uri, parameters, true);
	}
	
	/**
	 * Constructor
	 * 
	 * @param parameters
	 * 		the parameter objects array of this 
	 * 		AccessMethodResult
	 */
	protected AccessMethodResult(Mediator mediator, 
	String uri,	Object[] parameters, boolean exitOnError)
	{
		super();
		this.uri = uri;
		this.mediator = mediator;
		this.exitOnError = exitOnError;
	    this.parameters = parameters;
	}
    
    /**
     * Returns this AccessMethodResult's object parameter 
     * whose index is passed as parameter
     * 
     * @param index
     * 		this index of the object parameter
     * @return
     * 		this AccessMethodResult's object parameter 
     * 		whose index is passed as parameter
     */
    public Object getParameter(int index)
    {
    	if( index < 0 
    			|| this.parameters==null 
    			|| index >= this.parameters.length)
    	{
    		return null;
    	}
    	return this.parameters[index];
    }
    
    /**
     * Returns the length of the parameter objects
     * array of this AccessMethodResult
     * 
     * @return
     * 		the length of this parameter objects
     * 		array 
     */
    public int length()
    {
    	return this.parameters==null
    			?0:this.parameters.length;
    }
    
	/**
	 * Registers the exception passed as parameter 
	 * in this AccessMethodResult
	 * 
	 * @param exception
	 * 		the exception to register
	 */
	public void registerException(Exception exception)
	{
    	if(exceptions == null)
    	{
    		exceptions = new LinkedList<Exception>();
    	}
    	exceptions.add(exception);
	}
	
	/**
	 * Returns an unmodifiable list of {@link Exception}s registered
	 * on this AccessMethodResult
	 * 
	 * @return
	 * 		the list of registered {@link Exception}s
	 */
	public List<Exception> Exceptions()
	{
    	if(exceptions == null)
    	{
    		return Collections.<Exception>emptyList();
    	}
    	return Collections.<Exception>unmodifiableList(
    			(List<Exception>)this.exceptions);
	}
	
	/**
	 * Creates and returns this AccessMethodResult's 
	 * {@link AccessMethodResponse} 
	 */
	public AccessMethodResponse createSnaResponse() 
	{ 
		AccessMethodResponse.Status status = (exceptions!=null && exceptions.size()>0)
				? AccessMethodResponse.Status.ERROR: AccessMethodResponse.Status.SUCCESS;
		
		AccessMethodResponse response = this.createSnaResponse(status);
		
		if(exceptions!=null && exceptions.size()>0)
		{
			Iterator<Exception> iterator = exceptions.iterator();			
			JSONArray exceptionsArray = new JSONArray();
			
			while(iterator.hasNext())
			{
				Exception exception = iterator.next();				
				JSONObject exceptionObject = new JSONObject();				
				exceptionObject.put("message", exception.getMessage());
				
				StringBuilder buffer = new StringBuilder();				
				StackTraceElement[] trace = exception.getStackTrace();
				
				int index = 0;
				int length = trace.length;
				
				for(;index < length; index++)
				{
					buffer.append(trace[index].toString());
					buffer.append("\n");
				}
				exceptionObject.put("trace", buffer.toString());
				exceptionsArray.put(exceptionObject);
			}
			response.setErrors(exceptionsArray);
		}
		response.setResponse(this.resultObject);		
		return response;
	}

	/**
	 * Sets this AccessMethodResult's JSONObject value
	 * 
	 * @param resultObject the JSONObject value to be set
	 */
	public void setAccessMethodObjectResult(JSONObject resultObject)
	{
	     this.resultObject = resultObject;
	}
	
	/**
	 * Returns this AccessMethodResult's JSONObject value
	 * 
	 * @return this result value
	 */
	 public JSONObject getAccessMethodObjectResult()
	 {
		 return this.resultObject;
	 }
	 
	/**
	 * Returns the array of parameters parameterizing 
	 * the calls using this AccessMethodResult
	 * 
	 * @return
	 * 		the array of parameters parameterizing 
	 * 		the calls using this AccessMethodResult
	 */
	public Object[] getParameters() 
	{
		return this.parameters;
	}
	
	/**
	 * Returns true if the execution has to be stopped
	 * when an error occurred ; returns false otherwise
	 * 
	 * @return
	 * 		<ul>
	 * 			<li>true if the execution has to be stopped
	 * 				on error</li>
	 * 			<li>false otherwise</li>
	 * 		</ul>
	 */
	public boolean exitOnError()
	{
		return this.exitOnError;
	}
	
	/**
	 * Returns true if at least one error has been thrown
	 * during the execution
	 * 
	 * @return
	 * 		<ul>
	 * 			<li>true at least one error has been 
	 * 				triggered</li>
	 * 			<li>false otherwise</li>
	 * 		</ul>
	 */
	public boolean hasError()
	{
		return this.exceptions!=null && 
				this.exceptions.size()>0;
	}

	/**
	 * @InheritedDoc
	 *
	 * @see PathElement#getPath()
	 */
    @Override
    public String getPath()
    {
	    return this.uri;
    }
}
