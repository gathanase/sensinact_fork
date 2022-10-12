/*********************************************************************
* Copyright (c) 2022 Kentyou and others
*
* This program and the accompanying materials are made
* available under the terms of the Eclipse Public License 2.0
* which is available at https://www.eclipse.org/legal/epl-2.0/
*
* SPDX-License-Identifier: EPL-2.0
**********************************************************************/
package org.eclipse.sensinact.sensorthings.sensing.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Sensor extends NameDescription {

    public String encodingType;
    public Object metadata;

    @JsonProperty("Datastreams@iot.navigationLink")
    public String datastreamsLink;
}
