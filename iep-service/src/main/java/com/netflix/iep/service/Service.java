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

/**
 * Base interface representing a service.
 */
public interface Service {

  /**
   * Name of the service. This is mostly for human consumption when logging or otherwise showing
   * status about the service.
   */
  String name();

  /**
   * True if the service is healthy. This would typically be used for integrating with a high
   * level healthcheck to indicate if a server should take traffic.
   */
  boolean isHealthy();

  /**
   * Lifecycle state for the service.
   */
  State state();
}
