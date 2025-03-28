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

import java.lang.reflect.Type;

public class CreationException extends RuntimeException {

  public CreationException(Type type) {
    super(toMsg(type));
  }

  public CreationException(Type type, Throwable t) {
    super(toMsg(type), t);
  }

  public CreationException(String msg) {
    super(msg);
  }

  public CreationException(String msg, Throwable t) {
    super(msg, t);
  }

  private static String toMsg(Type type) {
    return "failed to create new instance of " + type;
  }
}
