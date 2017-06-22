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

package org.eclipse.sensinact.gateway.app.manager.osgi;

import org.eclipse.sensinact.gateway.common.bundle.Mediator;
import org.eclipse.sensinact.gateway.core.security.SecuredAccess;
import org.osgi.framework.*;

import java.util.Dictionary;

/**
 * @see Mediator
 *
 * @author Remi Druilhe
 */
public class AppServiceMediator extends Mediator {

    /**
     * @see Mediator#Mediator(BundleContext)
     */
    public AppServiceMediator(BundleContext context)  {
        super(context);
    }

    /**
     * Get the secured access from the OSGi registry
     * @return the secured access object
     */
    public SecuredAccess getSecuredAccess() {
        ServiceReference[] references = null;

        try {
            references = super.getContext().getServiceReferences(SecuredAccess.class.getCanonicalName(), null);
        } catch (InvalidSyntaxException e) {
            e.printStackTrace();
        }

        if(references != null) {
            if(references.length == 1) {
                return (SecuredAccess) super.getContext().getService(references[0]);
            }
        }

        return null;
    }

    /**
     * Get the array of serviceReference of a class currently started in OSGi
     * @param filter the filter
     * @return the array of service references
     */
    public ServiceReference[] getServiceReferences(String filter) {
        /*try {
            Collection<ServiceReference<DataProviderItf>> serv = super.getContext().getServiceReferences(DataProviderItf.class, null);

            for(ServiceReference<DataProviderItf> toto : serv) {
                System.out.println("----");

                String[] properties =  toto.getPropertyKeys();

                for(String prop : properties) {
                    System.out.println(prop + ": " + toto.getProperty(prop));
                }
            }
        } catch (InvalidSyntaxException e) {
            e.printStackTrace();
        }*/

        try {
            return super.getContext().getAllServiceReferences(null, filter);
        } catch (InvalidSyntaxException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Get the service from the registry using the service reference
     * @param serviceReference the service reference
     * @return the OSGi service
     */
    public Object getService(ServiceReference serviceReference) {
        return super.getContext().getService(serviceReference);
    }

    /**
     * @see BundleContext#registerService(Class, Object, Dictionary)
     */
    public ServiceRegistration registerService(String className, Object service, Dictionary<String, String> properties) {
        return super.getContext().registerService(className, service, properties);
    }

    /**
     * Register a service listener
     * @param serviceListener the class that is going to listen
     * @param filter the filter
     */
    public void addServiceListener(ServiceListener serviceListener, String filter) {
        try {
            super.getContext().addServiceListener(serviceListener, filter);
        } catch (InvalidSyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * Remove a service listener
     * @param serviceListener the class that was listening
     */
    public void removeServiceListener(ServiceListener serviceListener) {
        super.getContext().removeServiceListener(serviceListener);
    }
}
