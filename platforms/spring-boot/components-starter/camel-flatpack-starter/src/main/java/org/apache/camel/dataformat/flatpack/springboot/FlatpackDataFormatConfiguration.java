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
package org.apache.camel.dataformat.flatpack.springboot;

import javax.annotation.Generated;
import org.apache.camel.spring.boot.DataFormatConfigurationPropertiesCommon;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * The Flatpack data format is used for working with flat payloads (such as CSV,
 * delimited, or fixed length formats).
 * 
 * Generated by camel-package-maven-plugin - do not edit this file!
 */
@Generated("org.apache.camel.maven.packaging.SpringBootAutoConfigurationMojo")
@ConfigurationProperties(prefix = "camel.dataformat.flatpack")
public class FlatpackDataFormatConfiguration
        extends
            DataFormatConfigurationPropertiesCommon {

    /**
     * The flatpack pzmap configuration file. Can be omitted in simpler
     * situations, but its preferred to use the pzmap.
     */
    private String definition;
    /**
     * Delimited or fixed. Is by default false = delimited
     */
    private Boolean fixed = false;
    /**
     * Whether the first line is ignored for delimited files (for the column
     * headers). Is by default true.
     */
    private Boolean ignoreFirstRecord = true;
    /**
     * If the text is qualified with a character. Uses quote character by
     * default.
     */
    private String textQualifier;
    /**
     * The delimiter char (could be ; , or similar)
     */
    private String delimiter = ",";
    /**
     * Allows for lines to be shorter than expected and ignores the extra
     * characters
     */
    private Boolean allowShortLines = false;
    /**
     * Allows for lines to be longer than expected and ignores the extra
     * characters.
     */
    private Boolean ignoreExtraColumns = false;
    /**
     * References to a custom parser factory to lookup in the registry
     */
    private String parserFactoryRef;
    /**
     * Whether the data format should set the Content-Type header with the type
     * from the data format if the data format is capable of doing so. For
     * example application/xml for data formats marshalling to XML, or
     * application/json for data formats marshalling to JSon etc.
     */
    private Boolean contentTypeHeader = false;

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public Boolean getFixed() {
        return fixed;
    }

    public void setFixed(Boolean fixed) {
        this.fixed = fixed;
    }

    public Boolean getIgnoreFirstRecord() {
        return ignoreFirstRecord;
    }

    public void setIgnoreFirstRecord(Boolean ignoreFirstRecord) {
        this.ignoreFirstRecord = ignoreFirstRecord;
    }

    public String getTextQualifier() {
        return textQualifier;
    }

    public void setTextQualifier(String textQualifier) {
        this.textQualifier = textQualifier;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public Boolean getAllowShortLines() {
        return allowShortLines;
    }

    public void setAllowShortLines(Boolean allowShortLines) {
        this.allowShortLines = allowShortLines;
    }

    public Boolean getIgnoreExtraColumns() {
        return ignoreExtraColumns;
    }

    public void setIgnoreExtraColumns(Boolean ignoreExtraColumns) {
        this.ignoreExtraColumns = ignoreExtraColumns;
    }

    public String getParserFactoryRef() {
        return parserFactoryRef;
    }

    public void setParserFactoryRef(String parserFactoryRef) {
        this.parserFactoryRef = parserFactoryRef;
    }

    public Boolean getContentTypeHeader() {
        return contentTypeHeader;
    }

    public void setContentTypeHeader(Boolean contentTypeHeader) {
        this.contentTypeHeader = contentTypeHeader;
    }
}