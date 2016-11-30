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
package org.apache.camel.component.salesforce.api.dto;

import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;

import org.apache.camel.CamelContext;
import org.apache.camel.component.salesforce.annotation.SObjectMetadata;
import org.apache.camel.component.salesforce.dto.generated.Account;
import org.apache.camel.component.salesforce.dto.generated.Contact;
import org.apache.camel.component.salesforce.dto.generated.Merchandise__c;
import org.apache.camel.impl.DefaultCamelContext;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.junit.Test;

import static org.apache.camel.component.salesforce.api.dto.Metadata.classNameFrom;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class MetadataTest {

    @SObjectMetadata(name = "Bactrian", label = "Bactrian", labelPlural = "Bactrian")
    static class BactrianObject {
    }

    private final CamelContext camelContext = new DefaultCamelContext();

    @Test
    public void shouldDeriveClassNameFromPropertyKey() {
        assertEquals("ClassName with property should be supported", "package.JustType",
                classNameFrom("package.JustType.property"));
    }

    @Test
    public void shouldDetectObjectTypesFromMetadataAnnotation() {
        assertEquals("Should be based on class simple name", "Bactrian",
                Metadata.instance().labelOf(new BactrianObject()));
    }

    @Test
    public void shouldDetectObjectTypesFromSimpleClassName() {
        assertEquals("Should be based on class simple name", "Account", Metadata.instance().labelOf(new Account()));
        assertEquals("Should be based on class simple name", "Merchandise__c",
                Metadata.instance().labelOf(new Merchandise__c()));
    }

    @Test
    public void shouldEnumerateObjectFieldsFromAnnotations() {
        final Set<SObjectField> fields = Metadata.instance().fieldsOf(new Account());

        final SObjectField accountNumber = new SObjectField();
        accountNumber.setName("AccountNumber");

        final SObjectField website = new SObjectField();
        website.setName("Website");

        assertThat("Account metadata should contain AccountNumber field", fields, hasItem(accountNumber));
        assertThat("Account metadata should contain Website field", fields, hasItem(website));
    }

    @Test
    public void shouldEnumerateObjectFieldsFromProperties() {
        final Set<SObjectField> fields = Metadata.instance().fieldsOf(new Contact());

        final SObjectField firstName = new SObjectField();
        firstName.setName("FirstName");

        final SObjectField accountId = new SObjectField();
        accountId.setName("AccountId");

        assertThat("Account metadata should contain AccountNumber field", fields, hasItem(firstName));
        assertThat("Account metadata should contain Website field", fields, hasItem(accountId));
    }

    @Test
    public void shouldLoadPredeterminedMetadata() {
        final Map<Class<?>, Metadata.Info> predeterminedMetadata = Metadata.loadPredeterminedMetadata();

        assertNotNull("Should load predetermined metadata", predeterminedMetadata);

        assertThat("Should contain default Salesforce object 'Account'", predeterminedMetadata,
                containsMetadataFor(Account.class, false, null, "Account", "Accounts", "Account"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowForUnparsableKeysInMetadataDefinitions() {
        classNameFrom("JustType");
    }

    @Test
    public void shouldTransformConfigurationToMetadata() {
        final Properties configuration = new Properties();
        configuration.put("org.apache.camel.component.salesforce.dto.generated.Account.name", "Account");
        configuration.put("org.apache.camel.component.salesforce.dto.generated.Account.label", "Account");
        configuration.put("org.apache.camel.component.salesforce.dto.generated.Account.pluralForm", "Accounts");

        final Map<Class<?>, Metadata.Info> transformed = Metadata
                .transformConfigurationToMetadata(camelContext.getClassResolver(), configuration);

        assertNotNull("Should transform configuration to metadata", transformed);

        final Metadata.Info accountMetadata = transformed.get(Account.class);
        assertNotNull("Should transform Account metadata", accountMetadata);
    }

    private Matcher<Map<Class<?>, Metadata.Info>> containsMetadataFor(final Class<?> type, final Boolean custom,
            final String keyPrefix, final String label, final String labelPlural, final String name) {
        return new TypeSafeDiagnosingMatcher<Map<Class<?>, Metadata.Info>>() {

            @Override
            public void describeTo(final Description description) {
                description.appendText("Metadata to be found for: " + type.getName());
            }

            @Override
            protected boolean matchesSafely(final Map<Class<?>, Metadata.Info> data,
                    final Description mismatchDescription) {

                final Metadata.Info objectMetadata = data.get(type);
                if (objectMetadata == null) {
                    mismatchDescription.appendText("No metadata found for " + type.getName());

                    return false;
                }

                boolean ret = matches(custom, objectMetadata.custom, "custom", mismatchDescription);
                ret &= matches(label, objectMetadata.label, "label", mismatchDescription);
                ret &= matches(labelPlural, objectMetadata.labelPlural, "pluralForm", mismatchDescription);
                ret &= matches(name, objectMetadata.name, "name", mismatchDescription);

                return ret;
            }

            boolean matches(final Object expected, final Object got, final String property,
                    final Description mismatchDescription) {
                final boolean matches = Objects.equals(expected, got);

                if (!matches) {
                    mismatchDescription.appendText("Expected property value for property ").appendValue(property)
                            .appendText(" be ").appendValue(expected).appendText(", but got: ").appendValue(got)
                            .appendText("\n");
                }

                return matches;
            }

        };
    }
}
