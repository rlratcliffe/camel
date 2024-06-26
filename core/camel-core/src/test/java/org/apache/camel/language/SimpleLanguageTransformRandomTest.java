/*
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
package org.apache.camel.language;

import org.apache.camel.ContextTestSupport;
import org.apache.camel.builder.RouteBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit test routing with simple language using random function which will reuse cached expressions.
 */
public class SimpleLanguageTransformRandomTest extends ContextTestSupport {

    @Test
    public void testSimpleTransform() {
        int out = template.requestBodyAndHeader("direct:start", "Random number", "max", "5", int.class);
        assertTrue(out <= 5);

        out = template.requestBodyAndHeader("direct:start", "Random number", "max", "20", int.class);
        assertTrue(out <= 20);

        out = template.requestBodyAndHeader("direct:start", "Random number", "max", "30", int.class);
        assertTrue(out <= 30);
    }

    @Override
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            public void configure() {
                from("direct:start").transform().simple("${random(1,${header.max})}");
            }
        };
    }
}
