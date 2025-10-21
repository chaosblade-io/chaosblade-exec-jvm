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

import com.alibaba.chaosblade.exec.common.center.ManagerFactory;
import com.alibaba.chaosblade.exec.common.center.StatusManager;
import com.alibaba.chaosblade.exec.common.center.StatusMetric;
import com.alibaba.chaosblade.exec.common.transport.Request;
import com.alibaba.chaosblade.exec.common.transport.Response;
import com.alibaba.chaosblade.exec.common.transport.Response.Code;
import com.alibaba.chaosblade.exec.common.util.StringUtil;

/** @author Changjun Xiao */
public class StatusHandler implements RequestHandler {
  private StatusManager statusManager;

  public StatusHandler() {
    this.statusManager = ManagerFactory.getStatusManager();
  }

  @Override
  public String getHandlerName() {
    return "status";
  }

  @Override
  public Response handle(Request request) {
    String suid = request.getParam("suid");
    if (StringUtil.isBlank(suid)) {
      return Response.ofFailure(Code.ILLEGAL_PARAMETER, "suid must not be empty");
    }
    StatusMetric statusMetric = statusManager.getStatusMetricByUid(suid);
    if (statusMetric == null) {
      return Response.ofFailure(Code.NOT_FOUND, "data not found");
    }
    return Response.ofSuccess(String.valueOf(statusMetric.getCount()));
  }

  @Override
  public void unload() {}
}
