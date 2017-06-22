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
package org.eclipse.sensinact.gateway.util;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.osgi.framework.BundleContext;

/**
 * 
 */
public class PropertyUtils
{
    private static final Pattern PATTERN = Pattern.compile(
            "\\$\\(([^\\)\\(\\$]+)\\)");
    
    /**
	 * Search and return the value of the property 
	 * which key is passed as parameter ; The search order
	 * is : 
	 * 		- the {@link Properties} passed as parameter first
	 * 		- then the {@link BundleContext};
	 * 		- and finally in the system
	 *  
	 * @param context
	 * 		the {@link BundleContext} 
	 * @param properties
	 * 		the {@link Properties}
	 * @param key
	 * 		the searched property's key
	 * @return
	 * 		the searched property's value 
	 */
	public static String getProperty(BundleContext context, 
			Properties properties, String key)
	{	    	
		if(key == null)
		{
			return null;
		}	
		while(true)
	    {
	    	Matcher matcher = PATTERN.matcher(key);
	    	if(!matcher.find())
	    	{
	    		break;
	    	}
	    	int start = matcher.start();
	    	int end = matcher.end();
	    	
	    	StringBuilder builder = new StringBuilder();
	    	if(start > 0)
	    	{
	    		builder.append(key.substring(0, start));
	    	}
	    	String property = getProperty(context, properties, matcher.group(1));
	    	if(property == null)
	    	{
	    		return null;
	    	}
	    	builder.append(property);		    	
	    	if(end < key.length())
	    	{
	    		builder.append(key.substring(end, key.length()));
	    	}
	    	key = builder.toString();
	    }		
		String value = null;
		
		if((value =(properties==null?null:properties.getProperty(key)))==null
				&& (value=context.getProperty(key))==null)
		{	
			value = System.getProperty(key);
		}
		return value;
	}

}
