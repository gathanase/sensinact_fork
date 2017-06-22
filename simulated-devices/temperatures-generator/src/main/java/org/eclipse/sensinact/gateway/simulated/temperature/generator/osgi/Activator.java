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

package org.eclipse.sensinact.gateway.simulated.temperature.generator.osgi;

import org.eclipse.sensinact.gateway.simulated.temperature.generator.internal.TemperaturesGeneratorAbstractPacket;
import org.eclipse.sensinact.gateway.simulated.temperature.generator.parser.DataParser;
import org.eclipse.sensinact.gateway.simulated.temperature.generator.reader.TemperaturesGeneratorPacket;
import org.eclipse.sensinact.gateway.simulated.temperature.generator.thread.TemperaturesGeneratorThreadManager;
import org.eclipse.sensinact.gateway.common.bundle.AbstractActivator;
import org.eclipse.sensinact.gateway.common.bundle.Mediator;
import org.eclipse.sensinact.gateway.core.SensiNactResourceModelConfiguration.BuildPolicy;
import org.eclipse.sensinact.gateway.generic.ExtModelConfiguration;
import org.eclipse.sensinact.gateway.generic.ExtModelInstanceBuilder;
import org.eclipse.sensinact.gateway.generic.local.LocalProtocolStackEndpoint;

import org.osgi.framework.BundleContext;


import java.util.Collections;

public class Activator extends AbstractActivator<Mediator> {

    private static final String DEVICES_NUMBER = "org.eclipse.sensinact.simulated.generator.amount";

    private LocalProtocolStackEndpoint<TemperaturesGeneratorAbstractPacket> connector;
    private ExtModelConfiguration manager;
    private TemperaturesGeneratorThreadManager threadManager;

    public void doStart() throws Exception 
    {      	
        if(manager == null)
        {
        	manager = new ExtModelInstanceBuilder(
        	super.mediator, TemperaturesGeneratorPacket.class
                ).withResourceBuildPolicy((byte) 
                   (BuildPolicy.BUILD_NON_DESCRIBED.getPolicy()|
                    BuildPolicy.BUILD_COMPLETE_ON_DESCRIPTION.getPolicy())
                ).withServiceBuildPolicy((byte)(
                    BuildPolicy.BUILD_NON_DESCRIBED.getPolicy()|
                 	BuildPolicy.BUILD_ON_DESCRIPTION.getPolicy())
                ).withStartAtInitializationTime(true
        		).buildConfiguration("temperature-resource.xml",
        			Collections.<String,String>emptyMap());
        }
        if(connector == null)
        {
        	connector = 
        	new LocalProtocolStackEndpoint<TemperaturesGeneratorAbstractPacket>(
        	super.mediator);
        }

    	connector.connect(manager);

        DataParser dataParser = new DataParser(mediator);

        this.threadManager = new TemperaturesGeneratorThreadManager(connector,
                dataParser.createDeviceInfosSet(Integer.parseInt(
                mediator.getContext().getProperty(DEVICES_NUMBER))));

        this.threadManager.startThreads();
    }

    public void doStop() throws Exception 
    {
        this.threadManager.stopThreads();
    	connector.stop();
    }

    public Mediator doInstantiate(BundleContext context)
             
    {
        return new Mediator(context);
    }
}
