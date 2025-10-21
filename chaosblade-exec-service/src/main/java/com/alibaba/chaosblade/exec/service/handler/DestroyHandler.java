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
import com.alibaba.chaosblade.exec.common.center.ModelSpecManager;
import com.alibaba.chaosblade.exec.common.center.StatusManager;
import com.alibaba.chaosblade.exec.common.exception.ExperimentException;
import com.alibaba.chaosblade.exec.common.model.Model;
import com.alibaba.chaosblade.exec.common.model.ModelSpec;
import com.alibaba.chaosblade.exec.common.model.handler.PreDestroyInjectionModelHandler;
import com.alibaba.chaosblade.exec.common.transport.Request;
import com.alibaba.chaosblade.exec.common.transport.Response;
import com.alibaba.chaosblade.exec.common.transport.Response.Code;
import com.alibaba.chaosblade.exec.common.util.StringUtil;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** @author Changjun Xiao */
public class DestroyHandler implements RequestHandler {
  private static final Logger LOGGER = LoggerFactory.getLogger(DestroyHandler.class);
  private ModelSpecManager modelSpecManager;
  private StatusManager statusManager;

  public DestroyHandler() {
    this.modelSpecManager = ManagerFactory.getModelSpecManager();
    this.statusManager = ManagerFactory.getStatusManager();
  }

  @Override
  public String getHandlerName() {
    return "destroy";
  }

  @Override
  public Response handle(Request request) {
    String uid = request.getParam("suid");
    String target = request.getParam("target");
    String action = request.getParam("action");
    if (StringUtil.isBlank(uid)) {
      if (StringUtil.isBlank(target) || StringUtil.isBlank(action)) {
        return Response.ofFailure(
            Code.ILLEGAL_PARAMETER,
            "less necessary parameters, such as uid, target and" + " action");
      }
      return destroy(target, action);
    }
    return destroy(uid);
  }

  /**
   * Destroy experiments by target and action parameters
   *
   * @param target
   * @param action
   * @return
   */
  private Response destroy(String target, String action) {
    // search model by target and action and remove it
    Set<String> uids = statusManager.listUids(target, action);
    StringBuilder errMsg = new StringBuilder();
    StringBuilder successMsg = new StringBuilder();
    boolean success = true;
    for (String uid : uids) {
      Response response = destroy(uid);
      if (response.isSuccess()) {
        successMsg.append(uid).append(",");
      } else {
        errMsg.append(uid + ":" + response.getError()).append(",");
      }
    }
    if (success) {
      if (successMsg.length() > 0) {
        successMsg.deleteCharAt(successMsg.length() - 1);
      }
      return Response.ofSuccess(successMsg.toString());
    }
    return Response.ofFailure(Code.SERVER_ERROR, errMsg.toString());
  }

  /**
   * Destroy experiment by uid
   *
   * @param uid
   * @return
   */
  private Response destroy(String uid) {
    Model model = statusManager.removeExp(uid);
    return destroy(uid, model);
  }

  private Response destroy(String uid, Model model) {
    if (model == null) {
      return Response.ofSuccess("success");
    }
    ModelSpec modelSpec = this.modelSpecManager.getModelSpec(model.getTarget());
    if (modelSpec == null) {
      return Response.ofSuccess("success");
    }
    try {
      applyPreDestroyInjectionModelHandler(uid, modelSpec, model);
    } catch (ExperimentException ex) {
      return Response.ofFailure(Code.SERVER_ERROR, ex.getMessage());
    }
    return Response.ofSuccess("success");
  }

  @Override
  public void unload() {
    Set<String> uids = statusManager.getAllUids();
    for (String uid : uids) {
      Response response = destroy(uid);
      if (response.isSuccess()) {
        LOGGER.debug("destroy {} successfully when unload", uid);
      } else {
        LOGGER.warn("destroy {} failed because of {} when unload", uid, response.getError());
      }
    }
  }

  private void applyPreDestroyInjectionModelHandler(String uid, ModelSpec modelSpec, Model model)
      throws ExperimentException {
    if (modelSpec instanceof PreDestroyInjectionModelHandler) {
      ((PreDestroyInjectionModelHandler) modelSpec).preDestroy(uid, model);
    }
  }
}
