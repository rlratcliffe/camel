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
package org.apache.camel.management;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.support.service.ServiceHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DisabledOnOs(OS.AIX)
public class ManagedNamePatternIncludeHostNameTest extends ManagementTestSupport {

    @Override
    protected CamelContext createCamelContext() throws Exception {
        CamelContext context = super.createCamelContext();
        ServiceHelper.initService(context.getManagementStrategy());
        DefaultManagementObjectNameStrategy naming
                = (DefaultManagementObjectNameStrategy) context.getManagementStrategy().getManagementObjectNameStrategy();
        naming.setHostName("localhost");
        context.getManagementStrategy().getManagementAgent().setIncludeHostName(true);
        context.getManagementNameStrategy().setNamePattern("cool-#name#");
        return context;
    }

    @Test
    public void testManagedNamePattern() throws Exception {
        MBeanServer mbeanServer = getMBeanServer();

        assertTrue(context.getManagementName().startsWith("cool"));

        ObjectName on = ObjectName.getInstance(
                "org.apache.camel:context=localhost/" + context.getManagementName() + ",type=context,name=\""
                                               + context.getName() + "\"");
        assertTrue(mbeanServer.isRegistered(on), "Should be registered");
    }

    @Override
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            @Override
            public void configure() {
                from("direct:start").to("mock:result");
            }
        };
    }
}
