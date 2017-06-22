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

package org.eclipse.sensinact.gateway.app.basic.test;

import org.eclipse.sensinact.gateway.common.primitive.Description;
import org.eclipse.sensinact.gateway.core.ActionResource;
import org.eclipse.sensinact.gateway.core.AttributeDescription;
import org.eclipse.sensinact.gateway.core.ModelElementProxy;
import org.eclipse.sensinact.gateway.core.message.Recipient;
import org.eclipse.sensinact.gateway.core.method.ActResponse;
import org.eclipse.sensinact.gateway.core.method.GetResponse;
import org.eclipse.sensinact.gateway.core.method.SetResponse;
import org.eclipse.sensinact.gateway.core.method.AccessMethodResponse;
import org.eclipse.sensinact.gateway.core.method.SubscribeResponse;
import org.eclipse.sensinact.gateway.core.method.UnsubscribeResponse;
import org.eclipse.sensinact.gateway.common.constraint.Constraint;
import org.eclipse.sensinact.gateway.common.bundle.Mediator;
import org.eclipse.sensinact.gateway.common.primitive.ElementsProxy;
import org.eclipse.sensinact.gateway.common.primitive.Nameable;

import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Set;

class DisplayActionResource implements ActionResource {

    private TestSnaFunction function;
	private Mediator mediator;

    public DisplayActionResource(Mediator mediator, TestSnaFunction function) {
        this.function = function;
        this.mediator = mediator;
    }

    @Override
    public ActResponse act(Object... objects) {
        function.setDisplay((String) objects[0], (Integer) objects[1]);

        return new AppActionResponse(this.mediator, 
        		"/SimulatedTV_001/DisplayService_SimulatedTV_001/DISPLAY",
                AccessMethodResponse.Status.SUCCESS, 200);
    }

    @Override
    public GetResponse get(String s) {
        return null;
    }

    @Override
    public SetResponse set(String s, Object o) {
        return null;
    }

    @Override
    public SubscribeResponse subscribe(String s, Recipient recipient) {
        return null;
    }

    @Override
    public SubscribeResponse subscribe(String s, Recipient recipient, Set<Constraint> set) {
        return null;
    }

    @Override
    public UnsubscribeResponse unsubscribe(String s, String s1) {
        return null;
    }
    
    @Override
    public <D extends Description> D getDescription() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getPath() {
        return null;
    }

    @Override
    public Type getType() {
        return null;
    }

	/**
	 * @inheritDoc
	 *
	 * @see ModelElementProxy#element(java.lang.String)
	 */
    @Override
    public AttributeDescription element(String arg0){
	    return null;
    }

	/**
	 * @inheritDoc
	 *
	 * @see ModelElementProxy#elements()
	 */
    @Override
    public Enumeration<AttributeDescription> elements(){
	    return null;
    }

	/**
	 * @inheritDoc
	 *
	 * @see ElementsProxy#removeElement(java.lang.String)
	 */
	@Override
	public AttributeDescription removeElement(String name)
	{
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @inheritDoc
	 *
	 * @see ElementsProxy#addElement(Nameable)
	 */
	@Override
	public boolean addElement(AttributeDescription element)
	{
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @inheritDoc
	 *
	 * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])
	 */
	@Override
	public Object invoke(Object arg0, Method arg1, Object[] arg2)
	        throws Throwable
	{
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @inheritDoc
	 *
	 * @see ElementsProxy#isAccessible()
	 */
	@Override
	public boolean isAccessible()
	{
		return true;
	}
}
