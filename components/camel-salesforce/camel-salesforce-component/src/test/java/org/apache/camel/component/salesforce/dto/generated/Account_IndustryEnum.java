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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

//CHECKSTYLE:OFF
/**
 * Salesforce Enumeration DTO for picklist Industry
 */
public enum Account_IndustryEnum {

    // Agriculture
    AGRICULTURE("Agriculture"),
    // Apparel
    APPAREL("Apparel"),
    // Banking
    BANKING("Banking"),
    // Biotechnology
    BIOTECHNOLOGY("Biotechnology"),
    // Chemicals
    CHEMICALS("Chemicals"),
    // Communications
    COMMUNICATIONS("Communications"),
    // Construction
    CONSTRUCTION("Construction"),
    // Consulting
    CONSULTING("Consulting"),
    // Education
    EDUCATION("Education"),
    // Electronics
    ELECTRONICS("Electronics"),
    // Energy
    ENERGY("Energy"),
    // Engineering
    ENGINEERING("Engineering"),
    // Entertainment
    ENTERTAINMENT("Entertainment"),
    // Environmental
    ENVIRONMENTAL("Environmental"),
    // Finance
    FINANCE("Finance"),
    // Food & Beverage
    FOOD___BEVERAGE("Food & Beverage"),
    // Government
    GOVERNMENT("Government"),
    // Healthcare
    HEALTHCARE("Healthcare"),
    // Hospitality
    HOSPITALITY("Hospitality"),
    // Insurance
    INSURANCE("Insurance"),
    // Machinery
    MACHINERY("Machinery"),
    // Manufacturing
    MANUFACTURING("Manufacturing"),
    // Media
    MEDIA("Media"),
    // Not For Profit
    NOT_FOR_PROFIT("Not For Profit"),
    // Other
    OTHER("Other"),
    // Recreation
    RECREATION("Recreation"),
    // Retail
    RETAIL("Retail"),
    // Shipping
    SHIPPING("Shipping"),
    // Technology
    TECHNOLOGY("Technology"),
    // Telecommunications
    TELECOMMUNICATIONS("Telecommunications"),
    // Transportation
    TRANSPORTATION("Transportation"),
    // Utilities
    UTILITIES("Utilities");

    final String value;

    private Account_IndustryEnum(String value) {
        this.value = value;
    }

    @JsonValue
    public String value() {
        return this.value;
    }

    @JsonCreator
    public static Account_IndustryEnum fromValue(String value) {
        for (Account_IndustryEnum e : Account_IndustryEnum.values()) {
            if (e.value.equals(value)) {
                return e;
            }
        }
        throw new IllegalArgumentException(value);
    }

}
//CHECKSTYLE:ON