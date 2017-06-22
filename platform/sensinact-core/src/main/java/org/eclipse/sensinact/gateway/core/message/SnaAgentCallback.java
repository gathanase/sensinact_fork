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
package org.eclipse.sensinact.gateway.core.message;

/**
 * @author <a href="mailto:christophe.munilla@cea.fr">Christophe Munilla</a>
 */
public interface SnaAgentCallback extends MessageRegisterer
{    
	/**
	 * Processes the {@link SnaLifecycleMessageImpl} passed as 
	 * parameter
	 *  
	 * @param message the {@link SnaLifecycleMessageImpl} to be 
	 * processed
	 */
	void doHandle(SnaLifecycleMessageImpl message);
	
	/**
	 * Processes the {@link SnaUpdateMessageImpl} passed as 
	 * parameter
	 *  
	 * @param message the {@link SnaUpdateMessageImpl} to be 
	 * processed
	 */
	void doHandle(SnaUpdateMessageImpl message);

	/**
	 * Processes the {@link SnaErrorMessageImpl} passed as 
	 * parameter
	 *  
	 * @param message the {@link SnaErrorMessageImpl}  to be 
	 * processed
	 */
	void doHandle(SnaErrorMessageImpl message);

	/**
	 * Processes the {@link SnaResponseMessage} passed as 
	 * parameter
	 *  
	 * @param message the {@link SnaResponseMessage} to be 
	 * processed
	 */
	void doHandle(SnaResponseMessage message);
	
	/**
	 * Stops this {@link SnaAgent} callback
	 */
	void stop();
}