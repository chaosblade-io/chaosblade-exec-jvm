/*
 * Copyright 1999-2019 Alibaba Group Holding Ltd.
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

/**
 * @author Changjun Xiao
 */
public class DestroyHandler implements RequestHandler {

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
        if (StringUtil.isBlank(uid)) {
            return Response.ofFailure(Code.ILLEGAL_PARAMETER, "less experiment uid");
        }
        Model model = statusManager.removeExp(uid);

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
            return Response.ofFailure(Response.Code.SERVER_ERROR, ex.getMessage());
        }
        return Response.ofSuccess("success");
    }

    private void applyPreDestroyInjectionModelHandler(String uid, ModelSpec modelSpec, Model model)
        throws ExperimentException {
        if (modelSpec instanceof PreDestroyInjectionModelHandler) {
            ((PreDestroyInjectionModelHandler)modelSpec).preDestroy(uid, model);
        }
    }
}
