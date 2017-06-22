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

package org.eclipse.sensinact.gateway.datastore.api;

/**
 * 
 */
@SuppressWarnings("serial")
public class UnableToConnectToDataStoreException extends DataStoreException 
{
	/**
	 * Constructor
	 * 
	 * @param message
	 * 		the error message
	 */
	public UnableToConnectToDataStoreException(String message)
	{
		super(message);
	}

	/**
	 * Constructor
	 * 
	 * @param message
	 * 		the error message
	 * @param throwable
	 * 		wrapped exception that has made the current one thrown
	 */
	public UnableToConnectToDataStoreException(String message,Throwable throwable)
	{
		super(message,throwable);
	}
}
