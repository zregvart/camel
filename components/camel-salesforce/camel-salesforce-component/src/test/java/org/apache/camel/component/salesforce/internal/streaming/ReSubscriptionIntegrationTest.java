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
package org.apache.camel.component.salesforce.internal.streaming;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.component.salesforce.AbstractSalesforceTestBase;
import org.apache.camel.component.salesforce.SalesforceComponent;
import org.apache.camel.component.salesforce.dto.generated.Account;
import org.assertj.core.api.Assertions;
import org.awaitility.Awaitility;
import org.junit.Test;

public class ReSubscriptionIntegrationTest extends AbstractSalesforceTestBase {

    @Test
    public void shouldReSubscribeOnClientRecovery() throws Exception {
        final AtomicReference<Account> receivedAccount = new AtomicReference<>();
        final MockEndpoint mock = getMockEndpoint("mock:received");
        mock.whenAnyExchangeReceived(e -> receivedAccount.set(e.getMessage().getBody(Account.class)));
        mock.expectedMessageCount(1);

        // create test account
        final Account account1 = new Account();
        account1.setName("Account 1");

        template.sendBody("salesforce:createSObject", account1);
        // lets wait for a event to be received
        mock.assertIsSatisfied(10000);
        Assertions.assertThat(receivedAccount.get()).isNotNull();
        Assertions.assertThat(receivedAccount.get().getName()).isEqualTo("Account 1");

        // manually restart client, this happens on handshake errors or
        // disconnects
        final SalesforceComponent salesforce = (SalesforceComponent) context.getComponent("salesforce");
        final SubscriptionHelper subscriptionHelper = salesforce.getSubscriptionHelper();
        subscriptionHelper.restartClient();

        Awaitility.await().atMost(10, TimeUnit.SECONDS).until(() -> subscriptionHelper.client.isHandshook());

        // now that we've reconnected we should be subscribed
        mock.reset();
        mock.whenAnyExchangeReceived(e -> receivedAccount.set(e.getMessage().getBody(Account.class)));
        mock.expectedMessageCount(1);

        final Account account2 = new Account();
        account2.setName("Account 2");
        template.sendBody("salesforce:createSObject", account2);
        // lets wait for a event to be received
        mock.assertIsSatisfied(10000);
        Assertions.assertThat(receivedAccount.get()).isNotNull();
        Assertions.assertThat(receivedAccount.get().getName()).isEqualTo("Account 2");
    }

    @Override
    protected RouteBuilder doCreateRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("salesforce:CamelTestReSubscription?notifyForOperationCreate=true&sObjectName=Account&"
                    + "updateTopic=true&sObjectQuery=SELECT Id, Name FROM Account").to("mock:received");
            }
        };
    }
}
