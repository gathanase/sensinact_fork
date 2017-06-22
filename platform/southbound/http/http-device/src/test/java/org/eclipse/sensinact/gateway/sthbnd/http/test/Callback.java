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
package org.eclipse.sensinact.gateway.sthbnd.http.test;

import java.lang.reflect.Method;

public class Callback
{
	public final Object target;
	public final Method method;
	
	Callback(Object target, Method method)
	{
		this.target = target;
		this.method = method;
	}
	
	public Object invoke(Object[] parameters) throws Exception
	{
		this.method.setAccessible(true);
		return this.method.invoke(this.target, parameters);
	}
}