/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.component.salesforce.dto.generated;

import java.time.ZonedDateTime;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.apache.camel.component.salesforce.api.dto.AbstractSObjectBase;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Salesforce DTO for SObject Tasks__c
 */
@XStreamAlias("Tasks__c")
public class Tasks__c extends AbstractSObjectBase {

    // Start__c
    private ZonedDateTime Start__c;

    @JsonProperty("Start__c")
    public ZonedDateTime getStart__c() {
        return this.Start__c;
    }

    @JsonProperty("Start__c")
    public void setStart__c(ZonedDateTime Start__c) {
        this.Start__c = Start__c;
    }

    // Planned__c
    private ZonedDateTime Planned__c;

    @JsonProperty("Planned__c")
    public ZonedDateTime getPlanned__c() {
        return this.Planned__c;
    }

    @JsonProperty("Planned__c")
    public void setPlanned__c(ZonedDateTime Planned__c) {
        this.Planned__c = Planned__c;
    }

}
