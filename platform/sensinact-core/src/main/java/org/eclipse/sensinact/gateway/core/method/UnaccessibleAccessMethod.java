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
import java.util.Set;

import org.eclipse.sensinact.gateway.common.bundle.Mediator;
import org.eclipse.sensinact.gateway.common.execution.ErrorHandler;
import org.eclipse.sensinact.gateway.common.primitive.Describable;
import org.eclipse.sensinact.gateway.common.primitive.Description;
import org.eclipse.sensinact.gateway.common.primitive.Nameable;
import org.eclipse.sensinact.gateway.common.primitive.PathElement;

/**
 * An unaccessible {@link AccessMethod} implementation
 * 
 * @author <a href="mailto:christophe.munilla@cea.fr">Christophe Munilla</a>
 */
public class UnaccessibleAccessMethod implements AccessMethod
{	
	private final AccessMethod.Type type;
	private final String uri;
	/**
	 * Mediator used to interact with the OSGi host
	 * environment 
	 */
	protected final Mediator mediator;

	/**
	 * @param uri
	 * @param type
	 */
	public UnaccessibleAccessMethod(Mediator mediator, 
			String uri , AccessMethod.Type type)
	{
		this.mediator = mediator;
		this.type = type;
		this.uri = uri;
	}

	/**
	 * @inheritDoc
	 *
	 * @see Describable#getDescription()
	 */
	@Override
	public <D extends Description> D getDescription()
	{
		return null;
	}

	/**
	 * @inheritDoc
	 *
	 * @see Nameable#getName()
	 */
	@Override
	public String getName()
	{
		return this.type.name();
	}

	/**
	 * @inheritDoc
	 *
	 * @see AccessMethod#getType()
	 */
	@Override
	public Type getType()
	{
		return this.type;
	}
	
	/**
	 * @inheritDoc
	 *
	 * @see AccessMethod#invoke(java.lang.Object[])
	 */
	@Override
	public AccessMethodResponse invoke(Object[] parameters)
	{
    	AccessMethodResponse response = 
    		AccessMethodResponse.error( this.mediator, uri, type, 
    		AccessMethodResponse.FORBIDDEN_ERROR_CODE, 
    		"Unaccessible method", null);  
    	
		return response;
	}

	/**
	 * @inheritDoc
	 *
	 * @see AccessMethod#getSignatures()
	 */
	@Override
	public Set<Signature> getSignatures()
	{
		return Collections.<Signature>emptySet();
	}

	/**
	 * @inheritDoc
	 *
	 * @see AccessMethod#size()
	 */
	@Override
	public int size()
	{
		return 0;
	}

	/**
	 * @inheritDoc
	 *
	 * @see AccessMethod#getErrorHandler()
	 */
    @Override
    public ErrorHandler getErrorHandler()
    {
	    return null;
    }

	/**
	 * @inheritDoc
	 * 
	 * @see PathElement#getPath()
	 */
	@Override
	public String getPath() 
	{
		return this.uri;
	}


}
