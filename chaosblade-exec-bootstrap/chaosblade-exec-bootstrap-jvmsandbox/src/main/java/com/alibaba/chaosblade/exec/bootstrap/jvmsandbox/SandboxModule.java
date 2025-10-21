/*
 * Copyright 2025 The ChaosBlade Authors
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

package com.alibaba.chaosblade.exec.bootstrap.jvmsandbox;

import com.alibaba.chaosblade.exec.common.aop.Plugin;
import com.alibaba.chaosblade.exec.common.aop.PluginBean;
import com.alibaba.chaosblade.exec.common.aop.PluginLifecycleListener;
import com.alibaba.chaosblade.exec.common.aop.PointCut;
import com.alibaba.chaosblade.exec.common.center.ManagerFactory;
import com.alibaba.chaosblade.exec.common.model.ModelSpec;
import com.alibaba.chaosblade.exec.common.transport.Request;
import com.alibaba.chaosblade.exec.common.transport.Response;
import com.alibaba.chaosblade.exec.common.transport.Response.Code;
import com.alibaba.chaosblade.exec.common.util.PluginJarUtil;
import com.alibaba.chaosblade.exec.common.util.PluginLoader;
import com.alibaba.chaosblade.exec.common.util.PluginUtil;
import com.alibaba.chaosblade.exec.service.handler.DefaultDispatchService;
import com.alibaba.chaosblade.exec.service.handler.DispatchService;
import com.alibaba.jvm.sandbox.api.Information;
import com.alibaba.jvm.sandbox.api.Module;
import com.alibaba.jvm.sandbox.api.ModuleLifecycle;
import com.alibaba.jvm.sandbox.api.event.Event;
import com.alibaba.jvm.sandbox.api.event.Event.Type;
import com.alibaba.jvm.sandbox.api.filter.Filter;
import com.alibaba.jvm.sandbox.api.http.Http;
import com.alibaba.jvm.sandbox.api.resource.ModuleEventWatcher;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.Resource;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Changjun Xiao
 * @author hzyangshurui
 */
@Information(id = "chaosblade", version = "1.8.0", author = "Changjun Xiao", isActiveOnLoad = false)
public class SandboxModule implements Module, ModuleLifecycle, PluginLifecycleListener {

  private static Logger LOGGER = LoggerFactory.getLogger(SandboxModule.class);
  /** cache sandbox watch id */
  private static Map<String, Integer> watchIds = new ConcurrentHashMap<String, Integer>();

  @Resource private ModuleEventWatcher moduleEventWatcher;
  /** dispatcher service for handing request */
  private DispatchService dispatchService = new DefaultDispatchService();

  @Override
  public void onLoad() throws Throwable {
    LOGGER.info("load chaosblade module");
    ManagerFactory.getListenerManager().setPluginLifecycleListener(this);
    dispatchService.load();
    ManagerFactory.load();
  }

  @Override
  public void onUnload() throws Throwable {
    LOGGER.info("unload chaosblade module");
    dispatchService.unload();
    ManagerFactory.unload();
    watchIds.clear();
    LOGGER.info("unload chaosblade module successfully");
  }

  @Override
  public void onActive() throws Throwable {
    LOGGER.info("active chaosblade module");
    loadPlugins();
  }

  @Override
  public void onFrozen() throws Throwable {}

  @Override
  public void loadCompleted() {
    LOGGER.info("load chaosblade completed");
  }

  private void loadPlugins() throws Exception {
    List<Plugin> plugins =
        PluginLoader.load(Plugin.class, PluginJarUtil.getPluginFiles(getClass()));
    for (Plugin plugin : plugins) {
      try {
        PluginBean pluginBean = new PluginBean(plugin);
        final ModelSpec modelSpec = pluginBean.getModelSpec();
        // register model
        ManagerFactory.getModelSpecManager().registerModelSpec(modelSpec);
        ManagerFactory.getPluginManager().registerPlugin(pluginBean);
      } catch (Throwable e) {
        LOGGER.warn("Load " + plugin.getClass().getName() + " occurs exception", e);
      }
    }
  }

  @Http("/status")
  public void status(HttpServletRequest request, HttpServletResponse response) {
    service("status", request, response);
  }

  @Http("/create")
  public void create(HttpServletRequest request, HttpServletResponse response) {
    service("create", request, response);
  }

  @Http("/destroy")
  public void destroy(HttpServletRequest request, HttpServletResponse response) {
    service("destroy", request, response);
  }

  private void service(
      String command,
      HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse) {
    Request request;
    if ("POST".equalsIgnoreCase(httpServletRequest.getMethod())) {
      try {
        request = getRequestFromBody(httpServletRequest);
      } catch (IOException e) {
        Response response = Response.ofFailure(Code.ILLEGAL_PARAMETER, e.getMessage());
        output(httpServletResponse, response);
        return;
      }
    } else {
      request = getRequestFromParams(httpServletRequest);
    }
    Response response = dispatchService.dispatch(command, request);
    output(httpServletResponse, response);
  }

  private void output(HttpServletResponse httpServletResponse, Response response) {
    PrintWriter writer = null;
    try {
      writer = httpServletResponse.getWriter();
      writer.println(response.toString());
    } catch (Exception e) {
      LOGGER.warn("response exception", e);
    } finally {
      if (writer != null) {
        writer.close();
      }
    }
  }

  private Request getRequestFromParams(HttpServletRequest httpServletRequest) {
    Request request;
    request = new Request();
    Map<String, String[]> parameterMap = httpServletRequest.getParameterMap();
    Set<Entry<String, String[]>> entries = parameterMap.entrySet();
    for (Entry<String, String[]> entry : entries) {
      String value = "";
      String[] values = entry.getValue();
      if (values.length > 0) {
        value = values[0];
      }
      request.addParam(entry.getKey(), value);
    }
    return request;
  }

  private Request getRequestFromBody(HttpServletRequest httpServletRequest) throws IOException {
    ServletInputStream inputStream = httpServletRequest.getInputStream();
    MapType mapType =
        TypeFactory.defaultInstance().constructMapType(HashMap.class, String.class, String.class);
    Map<String, String> parameters = new ObjectMapper().readValue(inputStream, mapType);
    Request request = new Request();
    request.addParams(parameters);
    return request;
  }

  @Override
  public void add(PluginBean plugin) {
    PointCut pointCut = plugin.getPointCut();
    if (pointCut == null) {
      return;
    }
    String enhancerName = plugin.getEnhancer().getClass().getSimpleName();
    Filter filter = SandboxEnhancerFactory.createFilter(enhancerName, pointCut);
    // add after event listener. For the after event, the reason for adding the before event is to
    // cache the
    // necessary parameters.
    if (plugin.isAfterEvent()) {
      int watcherId =
          moduleEventWatcher.watch(
              filter,
              SandboxEnhancerFactory.createAfterEventListener(plugin),
              Type.BEFORE,
              Type.RETURN);
      watchIds.put(PluginUtil.getIdentifierForAfterEvent(plugin), watcherId);
    } else {
      int watcherId =
          moduleEventWatcher.watch(
              filter, SandboxEnhancerFactory.createBeforeEventListener(plugin), Event.Type.BEFORE);
      watchIds.put(PluginUtil.getIdentifier(plugin), watcherId);
    }
  }

  @Override
  public void delete(PluginBean plugin) {
    if (plugin.getPointCut() == null) {
      return;
    }
    String identifier;
    // remove the after event plugin
    if (plugin.isAfterEvent()) {
      identifier = PluginUtil.getIdentifierForAfterEvent(plugin);
    } else {
      identifier = PluginUtil.getIdentifier(plugin);
    }
    Integer watcherId = watchIds.get(identifier);
    if (watcherId != null) {
      moduleEventWatcher.delete(watcherId);
    }
    watchIds.remove(identifier);
  }
}
