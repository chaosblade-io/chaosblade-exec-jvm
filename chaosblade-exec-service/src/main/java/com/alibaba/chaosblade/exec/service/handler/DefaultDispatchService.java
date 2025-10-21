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

package com.alibaba.chaosblade.exec.service.handler;

import com.alibaba.chaosblade.exec.common.transport.Request;
import com.alibaba.chaosblade.exec.common.transport.Response;
import com.alibaba.chaosblade.exec.common.util.JsonUtil;
import com.alibaba.chaosblade.exec.common.util.StringUtil;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** @author Changjun Xiao */
public class DefaultDispatchService implements DispatchService {
  private static final Logger LOGGER = LoggerFactory.getLogger(DefaultDispatchService.class);
  private ConcurrentHashMap<String, RequestHandler> handles = new ConcurrentHashMap();

  private void registerHandler(RequestHandler requestHandler) {
    String handlerName = requestHandler.getHandlerName();
    RequestHandler handler = this.handles.putIfAbsent(handlerName, requestHandler);
    if (handler == null) {
      LOGGER.info("load {} handler", handlerName);
    } else {
      LOGGER.warn("{} handler exists, skip", handler);
    }
  }

  @Override
  public Response dispatch(String command, Request request) {
    if (StringUtil.isBlank(command)) {
      return Response.ofFailure(Response.Code.ILLEGAL_PARAMETER, "less request command");
    }
    try {
      String requestJson = JsonUtil.writer().writeValueAsString(request);
      LOGGER.info("command: {}, request: {}", command, requestJson);
    } catch (Throwable e) {
      LOGGER.warn("marshal request failed, command: {}", command, e);
    }
    RequestHandler handler = this.handles.get(command);
    if (handler == null) {
      return Response.ofFailure(Response.Code.NOT_FOUND, command + " command not found");
    }
    try {
      return handler.handle(request);
    } catch (Throwable e) {
      LOGGER.warn("handle {} request exception", command, e);
      return Response.ofFailure(Response.Code.SERVER_ERROR, e.getMessage());
    }
  }

  @Override
  public void load() {
    registerHandler(new CreateHandler());
    registerHandler(new DestroyHandler());
    registerHandler(new StatusHandler());
  }

  @Override
  public void unload() {
    Set<Entry<String, RequestHandler>> entries = handles.entrySet();
    for (Entry<String, RequestHandler> entry : entries) {
      entry.getValue().unload();
    }
    handles.clear();
  }
}
