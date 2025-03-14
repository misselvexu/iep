/*
 * Copyright 2014-2025 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.netflix.iep.config;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.Inet6Address;
import java.net.InetAddress;

@RunWith(JUnit4.class)
public class NetflixEnvironmentTest {

  @Test
  public void checkMethods() throws Exception {
    ClassLoader cl = Thread.currentThread().getContextClassLoader();
    Thread.currentThread().setContextClassLoader(null);
    try {
      for (Method method : NetflixEnvironment.class.getMethods()) {
        if (Modifier.isStatic(method.getModifiers()) && method.getParameterCount() == 0) {
          try {
            method.invoke(null);
          } catch (Exception e) {
            throw new RuntimeException("failed to invoke " + method.getName(), e);
          }
        }
      }
    } finally {
      Thread.currentThread().setContextClassLoader(cl);
    }
  }

  @Test
  public void hostAndIPv6() throws Exception {
    String host = ConfigManager.get().getString("netflix.iep.env.host");
    String ip = host.substring(1, host.length() - 1);
    InetAddress addr = InetAddress.getByName(ip);
    Assert.assertTrue(addr instanceof Inet6Address);
  }
}
