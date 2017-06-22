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
package org.eclipse.sensinact.gateway.common.primitive;


/**
 * A service with a state
 *
 * @param <S>
 * 		extended Enum type defining the status
 * 
 * @author <a href="mailto:christophe.munilla@cea.fr">Christophe Munilla</a>
 */
public interface Stateful<S extends Enum<S>>
{
	/**
     * Returns <code>&lt;S&gt;</code> typed status
     * of this stateful service
     * 
     * @return
     * 		this stateful service's status
     */
    S getStatus();
    
    /**
     * Sets the <code>&lt;S&gt;</code> typed status
     * of this stateful service
     * 
     * @param status
     * 		the <code>&lt;S&gt;</code> typed status to set
     * @return
     * 		the <code>&lt;S&gt;</code> typed status this 
     * 		stateful service
     * @throws InvalidValueException 
     * 
     */
    S setStatus(S status) throws InvalidValueException;
}
