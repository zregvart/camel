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
package org.apache.camel.component.salesforce.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Declares the metadata of a SObject data transfer object (DTO). For example:
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
 */
@Documented
@Retention(RUNTIME)
@Target(TYPE)
public @interface SObjectMetadata {

    /**
     * Metadata for SObject field.
     */
    @Documented
    @Retention(RUNTIME)
    @Target(TYPE)
    public @interface Field {
        /** The name of the field */
        String name();
    }

    /** Is the object custom */
    boolean custom() default false;

    /** Fields of the object */
    Field[] fields() default {};

    /** Label of the object */
    String label();

    /** Label in plural form of the object */
    String labelPlural();

    /** Name of the object */
    String name();
}
