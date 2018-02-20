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
package org.apache.camel.dataformat.protobuf.springboot;

import javax.annotation.Generated;
import org.apache.camel.spring.boot.DataFormatConfigurationPropertiesCommon;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * The Protobuf data format is used for serializing between Java objects and the
 * Google Protobuf protocol.
 * 
 * Generated by camel-package-maven-plugin - do not edit this file!
 */
@Generated("org.apache.camel.maven.packaging.SpringBootAutoConfigurationMojo")
@ConfigurationProperties(prefix = "camel.dataformat.protobuf")
public class ProtobufDataFormatConfiguration
        extends
            DataFormatConfigurationPropertiesCommon {

    /**
     * Name of class to use when unarmshalling
     */
    private String instanceClass;
    /**
     * Defines a content type format in which protobuf message will be
     * serialized/deserialized from(to) the Java been. The format can either be
     * native or json for either native protobuf or json fields representation.
     * The default value is native.
     */
    private String contentTypeFormat = "native";
    /**
     * Whether the data format should set the Content-Type header with the type
     * from the data format if the data format is capable of doing so. For
     * example application/xml for data formats marshalling to XML, or
     * application/json for data formats marshalling to JSon etc.
     */
    private Boolean contentTypeHeader = false;

    public String getInstanceClass() {
        return instanceClass;
    }

    public void setInstanceClass(String instanceClass) {
        this.instanceClass = instanceClass;
    }

    public String getContentTypeFormat() {
        return contentTypeFormat;
    }

    public void setContentTypeFormat(String contentTypeFormat) {
        this.contentTypeFormat = contentTypeFormat;
    }

    public Boolean getContentTypeHeader() {
        return contentTypeHeader;
    }

    public void setContentTypeHeader(Boolean contentTypeHeader) {
        this.contentTypeHeader = contentTypeHeader;
    }
}