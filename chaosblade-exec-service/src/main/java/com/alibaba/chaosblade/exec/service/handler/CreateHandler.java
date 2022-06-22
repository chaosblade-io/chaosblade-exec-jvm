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

import java.util.List;
import java.util.Set;

import com.alibaba.chaosblade.exec.common.aop.Plugin;
import com.alibaba.chaosblade.exec.common.aop.PluginBean;
import com.alibaba.chaosblade.exec.common.aop.PluginLifecycleListener;
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
import com.alibaba.chaosblade.exec.common.util.LogUtil;
import com.alibaba.chaosblade.exec.common.util.PluginJarUtil;
import com.alibaba.chaosblade.exec.common.util.PluginLoader;
import com.alibaba.chaosblade.exec.common.util.StringUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author changjun.xcj
 */
public class CreateHandler implements RequestHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateHandler.class);

    private ModelSpecManager modelSpecManager;
    private StatusManager statusManager;
    private volatile boolean unloaded;

    public CreateHandler() {
        this.modelSpecManager = ManagerFactory.getModelSpecManager();
        this.statusManager = ManagerFactory.getStatusManager();
    }

    @Override
    public String getHandlerName() {
        return "create";
    }

    /**
     * Handle request for creating chaos experiment
     *
     * @param request
     * @return
     */
    @Override
    public Response handle(Request request) {
        if (unloaded) {
            return Response.ofFailure(Code.ILLEGAL_STATE, "the agent is uninstalling");
        }
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
        // change level from info to debug if open debug mode
        checkAndSetLogLevel(request);
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

        return handleInjection(suid, model, modelSpec);
    }

    @Override
    public void unload() {
        unloaded = true;
    }

    /**
     * Set log level to debug if debug parameter is true
     *
     * @param request
     */
    private void checkAndSetLogLevel(Request request) {
        String debug = request.getParam("debug");
        if (Boolean.TRUE.toString().equalsIgnoreCase(debug)) {
            try {
                LogUtil.setDebug();
                LOGGER.info("change log level to debug");
            } catch (Exception e) {
                LOGGER.warn("set log level to debug failed", e);
            }
        } else {
            try {
                LogUtil.setInfo();
            } catch (Exception e) {
                LOGGER.warn("set log level to INFO failed", e);
            }
        }
    }

    /**
     * Handle injection
     *
     * @param suid
     * @param model
     * @return
     */
    private Response handleInjection(String suid, Model model, ModelSpec modelSpec) {
        RegisterResult result = this.statusManager.registerExp(suid, model);
        if (result.isSuccess()) {

            //TODO: 加载插件(走到这里，证明实验没被创建过，不用考虑幂等性问题)
            Response response = loadNecessaryPlugins(model);

            if (!response.isSuccess()) {
                return response;
            }

            // handle injection
            try {
                applyPreInjectionModelHandler(suid, modelSpec, model);
            } catch (ExperimentException ex) {
                this.statusManager.removeExp(suid);
                return Response.ofFailure(Response.Code.SERVER_ERROR, ex.getMessage());
            }

            return Response.ofSuccess(model.toString());
        }
        return Response.ofFailure(Response.Code.DUPLICATE_INJECTION, "the experiment exists");
    }

    private Response loadNecessaryPlugins(Model model) {
        List<Plugin> plugins = null;
        try {
            plugins = PluginLoader.load(Plugin.class, PluginJarUtil.getPluginFiles(getClass()));
        } catch (Exception e) {
            LOGGER.error("Load plugins occurs exception", e);
            //TODO 没加载到理论上应该结束，但是这种case其实不存在
            return Response.ofFailure(Response.Code.ILLEGAL_STATE, "spi load plugins occurs exception");
        }

        for (Plugin plugin : plugins) {
            try {
                PluginBean pluginBean = new PluginBean(plugin);
                final ModelSpec theModelSpec = pluginBean.getModelSpec();
                //TODO 插件匹配
                //step1: target级别的匹配
                if (!model.getTarget().equals(theModelSpec.getTarget())) {
                    continue;
                }
                //step2: matcher级别的匹配(插件名称，例如dubbo的consumer、provider等)
                Set<String> waitingForLoadPluginNames = model.getMatcher().getMatchers().keySet();
                if (!waitingForLoadPluginNames.isEmpty() && !waitingForLoadPluginNames.contains(plugin.getName())) {
                    continue;
                }
                // register model
                ManagerFactory.getModelSpecManager().registerModelSpec(theModelSpec);
                PluginLifecycleListener listener = getPluginLifecycleListener();
                listener.add(pluginBean);
            } catch (Throwable e) {
                LOGGER.warn("Load " + plugin.getClass().getName() + " occurs exception", e);
            }
        }
        return Response.ofSuccess("load plugins success");
    }

    private PluginLifecycleListener getPluginLifecycleListener() throws ExperimentException {
        PluginLifecycleListener listener = ManagerFactory.getListenerManager().getPluginLifecycleListener();
        if (listener == null) {
            throw new ExperimentException("can get plugin listener");
        }
        return listener;
    }

    /**
     * Pre-handle for injection
     *
     * @param suid
     * @param modelSpec
     * @param model
     * @throws ExperimentException
     */
    private void applyPreInjectionModelHandler(String suid, ModelSpec modelSpec, Model model)
        throws ExperimentException {
        if (modelSpec instanceof PreCreateInjectionModelHandler) {
            ((PreCreateInjectionModelHandler)modelSpec).preCreate(suid, model);
        }
    }
}
