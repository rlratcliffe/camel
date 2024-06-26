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
package org.apache.camel.component.platform.http;

import org.apache.camel.Processor;
import org.apache.camel.component.platform.http.spi.PlatformHttpConsumer;
import org.apache.camel.component.platform.http.spi.PlatformHttpEngine;
import org.apache.camel.support.CamelContextHelper;

public class JettyCustomPlatformHttpEngine implements PlatformHttpEngine {

    private int port;

    @Override
    public PlatformHttpConsumer createConsumer(PlatformHttpEndpoint platformHttpEndpoint, Processor processor) {
        if (port == 0) {
            JettyServerTest jettyServerTest = CamelContextHelper.mandatoryLookup(
                    platformHttpEndpoint.getCamelContext(),
                    JettyServerTest.JETTY_SERVER_NAME,
                    JettyServerTest.class);

            port = jettyServerTest.getServerPort();
        }

        return new JettyCustomPlatformHttpConsumer(platformHttpEndpoint, processor);
    }

    @Override
    public int getServerPort() {
        return port;
    }
}
