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
package com.netflix.iep.admin;

import com.netflix.iep.admin.endpoints.ResourcesEndpoint;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Simple side server used to provide debugging information for the main application.
 */
public class AdminServer implements AutoCloseable {

  private static final Logger LOGGER = LoggerFactory.getLogger(AdminServer.class);

  private static Map<String, Object> toMap(Set<EndpointMapping> mappings) {
    Map<String, Object> endpoints = new TreeMap<>();
    for (EndpointMapping mapping : mappings) {
      endpoints.put(mapping.getPath(), mapping.getObject());
    }
    return endpoints;
  }

  static InetSocketAddress resolve(String host, int port) throws UnknownHostException {
    return "*".equals(host)
        ? new InetSocketAddress(port)
        : new InetSocketAddress(InetAddress.getByName(host), port);
  }

  private final AdminConfig config;
  private final HttpServer server;

  public AdminServer(AdminConfig config, Set<EndpointMapping> mappings) throws IOException {
    this(config, toMap(mappings));
  }

  public AdminServer(AdminConfig config, Map<String, Object> endpoints)
      throws IOException {
    this.config = config;

    InetSocketAddress address = resolve(config.listenOn(), config.port());
    this.server = HttpServer.create(address, config.backlog());

    TreeSet<String> paths = new TreeSet<>(endpoints.keySet());
    for (String path : paths.descendingSet()) {
      Object obj = endpoints.get(path);
      HttpEndpoint endpoint = (obj instanceof HttpEndpoint)
          ? (HttpEndpoint) obj
          : new BasicHttpEndpoint(obj);
      createContext(path, new RequestHandler(path, endpoint));
    }

    SortedSet<String> resources = paths.stream()
        .map(p -> p.substring(1))
        .collect(Collectors.toCollection(TreeSet::new));
    resources.add("resources");
    createContext("/resources",
        new RequestHandler("/resources", new ResourcesEndpoint(resources)));

    StaticResourceHandler staticHandler = new StaticResourceHandler(
        Thread.currentThread().getContextClassLoader(),
        Collections.singletonMap("/ui", "static/index.html"));
    createContext("/static", staticHandler);
    createContext("/ui", staticHandler);

    createContext("/", new DefaultHandler(config));

    server.start();
    LOGGER.info("started on port {}", config.port());
  }

  private void createContext(String path, HttpHandler handler) {
    server.createContext(path, new AccessLogHandler(handler));
  }

  @Override public void close() throws Exception {
    LOGGER.info("shutting down admin on port {}", config.port());
    server.stop((int) config.shutdownDelay().toMillis());
  }
}
