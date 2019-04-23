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

import com.alibaba.chaosblade.exec.common.aop.PredicateResult;
import com.alibaba.chaosblade.exec.common.center.ManagerFactory;
import com.alibaba.chaosblade.exec.common.center.ModelSpecManager;
import com.alibaba.chaosblade.exec.common.center.RegisterResult;
import com.alibaba.chaosblade.exec.common.center.StatusManager;
import com.alibaba.chaosblade.exec.common.exception.ExperimentException;
import com.alibaba.chaosblade.exec.common.model.Model;
import com.alibaba.chaosblade.exec.common.model.ModelSpec;
import com.alibaba.chaosblade.exec.common.model.action.ActionSpec;
import com.alibaba.chaosblade.exec.common.model.handler.PreCreateInjectionModelHandler;
import com.alibaba.chaosblade.exec.common.transport.Request;
import com.alibaba.chaosblade.exec.common.transport.Response;
import com.alibaba.chaosblade.exec.common.transport.Response.Code;
import com.alibaba.chaosblade.exec.common.util.StringUtil;

/**
 * @author changjun.xcj
 */
public class CreateHandler implements RequestHandler {
    public static final String JVM = "jvm";
    private ModelSpecManager modelSpecManager;
    private StatusManager statusManager;

    public CreateHandler() {
        this.modelSpecManager = ManagerFactory.getModelSpecManager();
        this.statusManager = ManagerFactory.getStatusManager();
    }

    @Override
    public String getHandlerName() {
        return "create";
    }

    @Override
    public Response handle(Request request) {
        // check necessary arguments
        String suid = request.getParam("suid");
        if (StringUtil.isBlank(suid)) {
            return Response.ofFailure(Response.Code.ILLEGAL_PARAMETER, "less experiment argument");
        }
        String target = request.getParam("target");
        if (StringUtil.isBlank(target)) {
            return Response.ofFailure(Response.Code.ILLEGAL_PARAMETER, "less target argument");
        }
        String actionArg = request.getParam("action");
        if (StringUtil.isBlank(actionArg)) {
            return Response.ofFailure(Response.Code.ILLEGAL_PARAMETER, "less action argument");
        }
        // check the command supported or not
        ModelSpec modelSpec = this.modelSpecManager.getModelSpec(target);
        if (modelSpec == null) {
            return Response.ofFailure(Response.Code.ILLEGAL_PARAMETER, "the target not supported");
        }
        ActionSpec actionSpec = modelSpec.getActionSpec(actionArg);
        if (actionSpec == null) {
            return Response.ofFailure(Code.NOT_FOUND, "the action not supported");
        }
        // parse request to model
        Model model = ModelParser.parseRequest(target, request, actionSpec);
        // check command arguments
        PredicateResult predicate = modelSpec.predicate(model);
        if (!predicate.isSuccess()) {
            return Response.ofFailure(Response.Code.ILLEGAL_PARAMETER, predicate.getErr());
        }
        // handle injection
        try {
            applyPreInjectionModelHandler(suid, modelSpec, model);
        } catch (ExperimentException ex) {
            return Response.ofFailure(Response.Code.SERVER_ERROR, ex.getMessage());
        }

        return handleInjection(suid, model);
    }

    private Response handleInjection(String suid, Model model) {
        RegisterResult result = this.statusManager.registerExp(suid, model);
        if (result.isSuccess()) {
            return Response.ofSuccess(model.toString());
        }
        return Response.ofFailure(Response.Code.DUPLICATE_INJECTION, "the experiment exists");
    }

    private void applyPreInjectionModelHandler(String suid, ModelSpec modelSpec, Model model)
        throws ExperimentException {
        if (modelSpec instanceof PreCreateInjectionModelHandler) {
            ((PreCreateInjectionModelHandler)modelSpec).preCreate(suid, model);
        }
    }
}
