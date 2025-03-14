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
package com.netflix.iep.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

/**
 * Manager to keep track of long running services that make up an application. The services are
 * expected to start with the application and run for the life of the process. Service lifecycle
 * is managed via the DI framework, typically via PostConstruct and PreDestroy annotations. All
 * services should be injected into the set for this manager.
 */
public class ServiceManager {

  private final List<Service> services;

  public ServiceManager(Set<Service> serviceSet) {
    services = new ArrayList<>();
    services.addAll(serviceSet);
    services.sort(Comparator.comparing(Service::name));
  }

  public List<Service> services() {
    return Collections.unmodifiableList(services);
  }

  public boolean isHealthy() {
    boolean healthy = true;
    for (Service service : services) {
      healthy = healthy && service.isHealthy();
    }
    return healthy;
  }
}
