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

package org.eclipse.sensinact.gateway.simulated.light.osgi;

import java.util.Collections;

import org.eclipse.sensinact.gateway.simulated.light.internal.LightConfig;
import org.eclipse.sensinact.gateway.simulated.light.swing.LightPanel;
import org.osgi.framework.BundleContext;


import org.eclipse.sensinact.gateway.common.bundle.AbstractActivator;
import org.eclipse.sensinact.gateway.common.bundle.Mediator;
import org.eclipse.sensinact.gateway.generic.local.LocalProtocolStackEndpoint;
import org.eclipse.sensinact.gateway.generic.ExtModelConfiguration;
import org.eclipse.sensinact.gateway.generic.ExtModelInstanceBuilder;
import org.eclipse.sensinact.gateway.generic.packet.Packet;

public class Activator extends AbstractActivator<Mediator>
{
    private static final String GUI_ENABLED = "org.eclipse.sensinact.simulated.gui.enabled";

    private LocalProtocolStackEndpoint<Packet> connector;
    private LightConfig config;
    private LightPanel lightPanel;

    public void doStart() throws Exception
    {
        config = new LightConfig();
        
        ExtModelConfiguration manager = 
        new ExtModelInstanceBuilder(super.mediator,Packet.class
            ).withStartAtInitializationTime(true
            ).buildConfiguration("light-resource.xml",
        		Collections.<String,String>emptyMap());
        
        connector = new LocalProtocolStackEndpoint<Packet>(super.mediator);
        connector.addInjectableInstance(LightConfig.class, config); 
        connector.connect(manager);
        
        if(mediator.getContext().getProperty(GUI_ENABLED).equals("true"))
        {
            lightPanel = new LightPanel();
            config.addListener(lightPanel);
        }
    }

    public void doStop() throws Exception 
    {
    	connector.stop();
    	connector = null;
    	
    	config.removeListener(lightPanel);
        if(lightPanel != null)
        {
            lightPanel.stop();
        }
    }

    public Mediator doInstantiate(BundleContext context)
             
    {
        return new Mediator(context);
    }
}
