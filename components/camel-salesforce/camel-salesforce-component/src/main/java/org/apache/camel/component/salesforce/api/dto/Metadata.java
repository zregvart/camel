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

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.camel.component.salesforce.annotation.SObjectMetadata;
import org.apache.camel.component.salesforce.annotation.SObjectMetadata.Field;
import org.apache.camel.impl.DefaultClassResolver;
import org.apache.camel.spi.ClassResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Fetches the metadata for SObjects defined in the form of annotation on the SObject DTO class
 * ({@link SObjectMetadata}) or `.properties` file located at:
 * <code>META-INF/org/apache/camel/salesforce/metadata.properties</code>
 * <p>
 * The annotation and the properties file are equal in functionality it is up to the user what makes the most sense for
 * the given use case.
 * <p>
 * For example if there are packaged DTO classes already present in the classpath and the user cannot modify/replace
 * them then the easiest option might be to place the afore mentioned properties file in the classpath. Otherwise
 * annotation might be prefered option.
 * <p>
 * To use the properties file, create and place in classpath a JAR file containing
 * <code>META-INF/org/apache/camel/salesforce/metadata.properties</code>. The properties file syntax needs to be:
 *
 * <pre>
 * &lt;fully qualified class name&gt;.name=&lt;SObject name&gt;
 * &lt;fully qualified class name&gt;.label=&lt;SObject label&gt;
 * &lt;fully qualified class name&gt;.labelPlural=&lt;label in plural form&gt;
 * &lt;fully qualified class name&gt;.custom=&lt;true|false is SObject custom&gt;
 * &lt;fully qualified class name&gt;.fields=&lt;comma separated field names&gt;
 * </pre>
 *
 * To use the annotation place it on the class declaration, for example:
 *
 * <pre>
 * &#64;SObjectMetadata(custom = false, label = "Account", labelPlural = "Accounts", name = "Account",
 * fields = {
 *   &#64;Field(name = "Id"),
 *   &#64;Field(name = "Name"),
 *   &#64;Field(name = "Type"),
 *   ...
 * })
 * public class Account extends SObjectBase { ... }
 * </pre>
 * <p>
 * To use the Metadata use the `org.apache.camel.component.salesforce.api.dto.Metadata` class for example:
 * <p>
 *
 * <pre>
 * Account account = ...
 * Metadata metadata = new Metadata(camelContext);
 * Set&lt;SObjectField&gt; fields = metadata.fieldsOf(account);
 * </pre>
 * <p>
 * Note that even though `fieldsOf` method returns Set of SObjectField's only the `name` property is set.
 */
public final class Metadata {

    /**
     * Represents all metadata known for one SObject.
     */
    public static final class Info {

        /** is the SObject custom */
        public final Boolean custom;

        /** fields of SObject (unmodifiable) */
        public final Set<SObjectField> fields;

        /** label of SObject */
        public final String label;

        /** label in plural form of SObject */
        public final String labelPlural;

        /** name of SObject */
        public final String name;

        private Info(final Boolean custom, final String label, final String labelPlural, final String name,
                final Set<SObjectField> fields) {
            this.custom = custom;
            this.label = label;
            this.labelPlural = labelPlural;
            this.name = name;
            this.fields = Collections.unmodifiableSet(Optional.ofNullable(fields).orElse(Collections.emptySet()));
        }

        private Info(final Class<?> type) {
            this(null, type.getSimpleName(), null, type.getSimpleName(), null);
        }

        private Info(final Map<String, String> properties) {
            this(booleanValue(properties, "custom"), stringValue(properties, "label"),
                    stringValue(properties, "labelPlural"), stringValue(properties, "name"),
                    setValue(properties, "fields"));
        }

        private Info(final SObjectMetadata annotationMetadata) {
            this(annotationMetadata.custom(), annotationMetadata.label(), annotationMetadata.labelPlural(),
                    annotationMetadata.name(), objectFields(annotationMetadata.fields()));
        }

        private static boolean booleanValue(final Map<String, String> properties, final String property) {
            return Boolean.valueOf(properties.get(property));
        }

        private static Set<SObjectField> namesToFields(final Stream<String> names) {
            return names.map(f -> {
                final SObjectField field = new SObjectField();
                field.setName(f);

                return field;
            }).collect(Collectors.toSet());
        }

        private static Set<SObjectField> objectFields(final Field[] fields) {
            return namesToFields(Arrays.stream(fields).map(Field::name));
        }

        private static Set<SObjectField> setValue(final Map<String, String> properties, final String property) {
            return namesToFields(
                    Arrays.stream(Optional.ofNullable(properties.get(property)).orElse("").split("\\s*,\\s*")));
        }

        private static String stringValue(final Map<String, String> properties, final String property) {
            return properties.get(property);
        }
    }

    private static final Metadata INSTANCE = new Metadata();

    private static final Logger LOG = LoggerFactory.getLogger(Metadata.class);

    private static final String METADATA_PROPERTIES_LOCATION = "META-INF/org/apache/camel/salesforce/metadata.properties";

    private final Map<Class<?>, Info> staticMetadata;

    private Metadata() {
        staticMetadata = loadPredeterminedMetadata();
    }

    public static Metadata instance() {
        return INSTANCE;
    }

    static String classNameFrom(final String key) {
        final int lastIdx = key.lastIndexOf('.');

        if (lastIdx == -1) {
            throw new IllegalArgumentException(
                    "Given key `" + key + "` is not in the form <package>.<classname>.<property>");
        }

        return key.substring(0, lastIdx);
    }

    static Map<Class<?>, Info> loadMetadataFromUsing(final String path, final ClassResolver classResolver) {
        final Properties configuration = new Properties();

        try (InputStream stream = classResolver.loadResourceAsStream(path)) {
            if (stream != null) {
                configuration.load(stream);
            }
        } catch (final IOException e) {
            throw new IllegalStateException("Unable to load default metadata properties", e);
        }

        return transformConfigurationToMetadata(classResolver, configuration);
    }

    static Map<Class<?>, Info> loadPredeterminedMetadata() {
        final ClassResolver classResolver = new DefaultClassResolver();

        final Map<Class<?>, Info> metadata = new HashMap<>();
        // load metadata from properties file
        metadata.putAll(loadMetadataFromUsing(METADATA_PROPERTIES_LOCATION, classResolver));

        return metadata;
    }

    static Map<Class<?>, Info> transformConfigurationToMetadata(final ClassResolver classResolver,
            final Properties configuration) {
        @SuppressWarnings("unchecked")
        final Set<Entry<String, String>> configurationEntries = (Set) configuration.entrySet();

        // stream keys and values from properties file and covert them to Map of Class name as string and with
        // property-value Map for values, i.e. {"...dto.Acount", {"label": "Account", "labelPlural": "Accounts"}}
        final Map<String, Map<String, String>> propertiesByClassName = configurationEntries.stream()//
                .collect(Collectors.groupingBy(e -> classNameFrom(e.getKey())))//
                .entrySet().stream()//
                .collect(Collectors.toMap(Map.Entry::getKey, v -> {
                    final String className = v.getKey();
                    final int propertyIdx = className.length() + 1;
                    final List<Entry<String, String>> values = v.getValue();

                    final Map<String, String> propertyValues = values.stream()
                            .collect(Collectors.toMap(e -> e.getKey().substring(propertyIdx), Map.Entry::getValue));

                    return propertyValues;
                }));

        // holds any classes not found but declared in the properties file
        final Set<String> missing = new HashSet<>();

        // final metadata
        final Map<Class<?>, Info> metadataInfo = new HashMap<>();
        for (final Entry<String, Map<String, String>> info : propertiesByClassName.entrySet()) {
            final String className = info.getKey();

            final Class<?> type = classResolver.resolveClass(className);
            if (type == null) {
                missing.add(className);
                continue;
            }

            final Map<String, String> properties = info.getValue();
            final Info metadata = new Info(properties);
            metadataInfo.put(type, metadata);
        }

        if (!missing.isEmpty()) {
            LOG.warn("The following class names were placed in " + METADATA_PROPERTIES_LOCATION
                + ", but could not be loaded: {}", missing.stream().collect(Collectors.joining(", ")));
        }

        return metadataInfo;
    }

    public Set<SObjectField> fieldsOf(final Object object) {
        return infoFor(object).fields;
    }

    public Info infoFor(final Class<?> type) {
        final SObjectMetadata annotationMetadata = type.getAnnotation(SObjectMetadata.class);
        if (annotationMetadata != null) {
            return new Info(annotationMetadata);
        }

        final Info staticTypeMetadata = staticMetadata.get(type);
        if (staticTypeMetadata != null) {
            return staticTypeMetadata;
        }

        return new Info(type);
    }

    public Info infoFor(final Object object) {
        if (object == null) {
            throw new IllegalArgumentException("Object must be given (given null)");
        }

        return infoFor(object.getClass());
    }

    public String labelOf(final Object object) {
        return infoFor(object).label;
    }

    public String labelPluralOf(final Object object) {
        return infoFor(object).labelPlural;
    }
}
