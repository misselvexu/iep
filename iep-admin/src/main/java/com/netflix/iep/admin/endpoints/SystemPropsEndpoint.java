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
package com.netflix.iep.admin.endpoints;

import com.netflix.iep.admin.HttpEndpoint;

import java.util.Map;
import java.util.TreeMap;

/**
 * Endpoint for providing access to system properties.
 */
public class SystemPropsEndpoint implements HttpEndpoint {
  @Override public Object get() {
    return new TreeMap<>(System.getProperties());
  }

  @Override public Object get(String path) {
    Map<String, String> props = new TreeMap<>();
    System.getProperties()
        .entrySet()
        .stream()
        .filter(e -> e.getKey().toString().startsWith(path))
        .forEach(e -> props.put(e.getKey().toString(), e.getValue().toString()));
    return props;
  }
}
