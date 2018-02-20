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
package org.apache.camel.component.sjms.batch.springboot;

import javax.annotation.Generated;
import javax.jms.ConnectionFactory;
import org.apache.camel.spi.HeaderFilterStrategy;
import org.apache.camel.spring.boot.ComponentConfigurationPropertiesCommon;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * The sjms-batch component is a specialized for highly performant,
 * transactional batch consumption from a JMS queue.
 * 
 * Generated by camel-package-maven-plugin - do not edit this file!
 */
@Generated("org.apache.camel.maven.packaging.SpringBootAutoConfigurationMojo")
@ConfigurationProperties(prefix = "camel.component.sjms-batch")
public class SjmsBatchComponentConfiguration
        extends
            ComponentConfigurationPropertiesCommon {

    /**
     * A ConnectionFactory is required to enable the SjmsBatchComponent.
     */
    private ConnectionFactory connectionFactory;
    /**
     * Whether to startup the consumer message listener asynchronously, when
     * starting a route. For example if a JmsConsumer cannot get a connection to
     * a remote JMS broker, then it may block while retrying and/or failover.
     * This will cause Camel to block while starting routes. By setting this
     * option to true, you will let routes startup, while the JmsConsumer
     * connects to the JMS broker using a dedicated thread in asynchronous mode.
     * If this option is used, then beware that if the connection could not be
     * established, then an exception is logged at WARN level, and the consumer
     * will not be able to receive messages; You can then restart the route to
     * retry.
     */
    private Boolean asyncStartListener = false;
    /**
     * Specifies the interval between recovery attempts, i.e. when a connection
     * is being refreshed, in milliseconds. The default is 5000 ms, that is, 5
     * seconds.
     */
    private Integer recoveryInterval = 5000;
    /**
     * To use a custom org.apache.camel.spi.HeaderFilterStrategy to filter
     * header to and from Camel message.
     */
    @NestedConfigurationProperty
    private HeaderFilterStrategy headerFilterStrategy;
    /**
     * Whether the component should resolve property placeholders on itself when
     * starting. Only properties which are of String type can use property
     * placeholders.
     */
    private Boolean resolvePropertyPlaceholders = true;

    public ConnectionFactory getConnectionFactory() {
        return connectionFactory;
    }

    public void setConnectionFactory(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public Boolean getAsyncStartListener() {
        return asyncStartListener;
    }

    public void setAsyncStartListener(Boolean asyncStartListener) {
        this.asyncStartListener = asyncStartListener;
    }

    public Integer getRecoveryInterval() {
        return recoveryInterval;
    }

    public void setRecoveryInterval(Integer recoveryInterval) {
        this.recoveryInterval = recoveryInterval;
    }

    public HeaderFilterStrategy getHeaderFilterStrategy() {
        return headerFilterStrategy;
    }

    public void setHeaderFilterStrategy(
            HeaderFilterStrategy headerFilterStrategy) {
        this.headerFilterStrategy = headerFilterStrategy;
    }

    public Boolean getResolvePropertyPlaceholders() {
        return resolvePropertyPlaceholders;
    }

    public void setResolvePropertyPlaceholders(
            Boolean resolvePropertyPlaceholders) {
        this.resolvePropertyPlaceholders = resolvePropertyPlaceholders;
    }
}