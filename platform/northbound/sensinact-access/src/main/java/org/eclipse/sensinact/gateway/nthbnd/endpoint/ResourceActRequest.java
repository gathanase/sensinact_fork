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
package org.eclipse.sensinact.gateway.nthbnd.endpoint;

public class ResourceActRequest<F> extends ResourceRequest<F>
{
	private Object[] arguments;

	/**
	 * @param mediator
	 * @param responseFormat
	 * @param serviceProvider
	 * @param service
	 * @param resource
	 * @param arguments
	 */
	public ResourceActRequest(NorthboundMediator mediator,
			String serviceProvider,  String service, String resource,
			Object[] arguments)
	{
		super(mediator, serviceProvider, service, resource);		
		this.arguments = arguments;
	}
	
	/** 
	 * @inheritDoc
	 * 
	 * @see ServiceProvidersRequest#
	 * getExecutionArguments()
	 */
	@Override
	protected Argument[] getExecutionArguments() 
	{
		Argument[] superArguments = super.getExecutionArguments();	
		int length = superArguments==null?0:superArguments.length;
		Argument[] arguments = new Argument[length+1];
		if(length > 0)
		{
			System.arraycopy(superArguments, 0, arguments, 0, length);
		}
		arguments[length] = new Argument(Object[].class,this.arguments);
	    return arguments;
	}
	
	/**
	 * @inheritDoc
	 *
	 * @see ResourcesRequest#
	 * getMethod()
	 */
	@Override
	protected String getMethod()
	{
		return "act";
	}
	
}
