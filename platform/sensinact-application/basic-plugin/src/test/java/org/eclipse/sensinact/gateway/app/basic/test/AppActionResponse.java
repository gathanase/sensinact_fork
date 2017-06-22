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

import org.eclipse.sensinact.gateway.core.method.ActResponse;
import org.eclipse.sensinact.gateway.common.bundle.Mediator;

class AppActionResponse extends ActResponse {

    public AppActionResponse(Mediator mediator, String uri, Status status, int code) {
        super(mediator, uri, status, code);
    }
}
