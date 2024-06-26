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
package org.apache.camel.component.dynamicrouter.routing;

import org.apache.camel.AsyncCallback;
import org.apache.camel.Exchange;
import org.apache.camel.support.DefaultAsyncProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A {@link org.apache.camel.Producer} implementation to process exchanges for the Dynamic Router.
 */
public class DynamicRouterProducer extends DefaultAsyncProducer {

    private static final Logger LOG = LoggerFactory.getLogger(DynamicRouterProducer.class);

    /**
     * The {@link DynamicRouterComponent}.
     */
    private final DynamicRouterComponent component;

    /**
     * The configuration for the Dynamic Router.
     */
    private final DynamicRouterConfiguration configuration;

    /**
     * Create the {@link org.apache.camel.Producer} for the Dynamic Router with the supplied
     * {@link org.apache.camel.Endpoint} URI.
     *
     * @param endpoint  the {@link DynamicRouterEndpoint}
     * @param component the {@link DynamicRouterComponent}
     */
    public DynamicRouterProducer(final DynamicRouterEndpoint endpoint, DynamicRouterComponent component,
                                 final DynamicRouterConfiguration configuration) {
        super(endpoint);
        this.component = component;
        this.configuration = configuration;
        LOG.debug("Created producer for endpoint '{}', channel '{}'",
                endpoint.getEndpointUri(), configuration.getChannel());
    }

    /**
     * Process the exchange.
     *
     * @param  exchange  the exchange to process
     * @throws Exception if the consumer has a problem
     */
    @Override
    public void process(final Exchange exchange) throws Exception {
        component.getRoutingProcessor(configuration.getChannel())
                .process(exchange);
    }

    /**
     * Process the exchange, and use the {@link AsyncCallback} to signal completion.
     *
     * @param  exchange the exchange to process
     * @param  callback the {@link AsyncCallback} to signal when asynchronous processing has completed
     * @return          true to continue to execute synchronously, or false to continue to execute asynchronously
     */
    @Override
    public boolean process(final Exchange exchange, final AsyncCallback callback) {
        DynamicRouterProcessor routingProcessor = component.getRoutingProcessor(configuration.getChannel());
        if (configuration.isSynchronous()) {
            try {
                routingProcessor.process(exchange);
            } catch (Exception e) {
                exchange.setException(e);
            } finally {
                callback.done(true);
            }
            return true;
        } else {
            return routingProcessor.process(exchange, callback);
        }
    }

    /**
     * Create a {@link DynamicRouterProducer} instance.
     */
    public static class DynamicRouterProducerFactory {

        /**
         * Create the {@link org.apache.camel.Producer} for the Dynamic Router with the supplied
         * {@link org.apache.camel.Endpoint} URI.
         *
         * @param endpoint the {@link DynamicRouterEndpoint}
         */
        public DynamicRouterProducer getInstance(
                final DynamicRouterEndpoint endpoint,
                final DynamicRouterComponent component,
                final DynamicRouterConfiguration configuration) {
            return new DynamicRouterProducer(endpoint, component, configuration);
        }
    }
}
