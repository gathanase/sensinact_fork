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
package org.eclipse.sensinact.gateway.sthbnd.http.smpl;

import org.eclipse.sensinact.gateway.generic.Task.CommandType;
import org.eclipse.sensinact.gateway.sthbnd.http.annotation.HttpTaskConfiguration;
import org.eclipse.sensinact.gateway.sthbnd.http.task.HttpTask;

class RecurrentTaskConfigurator extends 
SimpleTaskConfigurator implements RecurrentHttpTaskConfigurator
{ 	
	private long period = 1000*60;
	private long delay = 1000;
	private long timeout = -1;
	private Class<? extends HttpTask> taskType;
	
	public RecurrentTaskConfigurator(
		SimpleHttpProtocolStackEndpoint endpoint,
		CommandType command, HttpTaskUrlConfigurator urlBuilder, 
		Class<? extends HttpTask> taskType,
		long period, long delay, long timeout,
		HttpTaskConfiguration annotation)
	{	
		super(endpoint, null, command, urlBuilder,annotation);
		this.setTaskType(taskType);
		this.setPeriod(period);
		this.setDelay(delay);
		this.setTimeout(timeout);
	}

	/**
	 * @return the period
	 */
	public long getPeriod() {
		return period;
	}

	/**
	 * @param period the period to set
	 */
	public void setPeriod(long period) {
		this.period = period;
	}

	/**
	 * @return the delay
	 */
	public long getDelay() {
		return delay;
	}

	/**
	 * @param delay the delay to set
	 */
	public void setDelay(long delay) {
		this.delay = delay;
	}

	/**
	 * @return the timeout
	 */
	public long getTimeout() {
		return timeout;
	}

	/**
	 * @param timeout the timeout to set
	 */
	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

	/**
	 * @return 
	 *     the taskType
	 */
	public Class<? extends HttpTask> getTaskType()
	{
		return taskType;
	}

	/**
	 * @param taskType 
	 *     the taskType to set
	 */
	public void setTaskType(Class<? extends HttpTask> taskType)
	{
		this.taskType = taskType;
	}
	
}