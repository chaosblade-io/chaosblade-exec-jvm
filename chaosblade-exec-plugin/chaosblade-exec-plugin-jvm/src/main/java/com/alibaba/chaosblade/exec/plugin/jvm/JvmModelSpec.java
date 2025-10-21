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

package com.alibaba.chaosblade.exec.plugin.jvm;

import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.common.exception.ExperimentException;
import com.alibaba.chaosblade.exec.common.model.Model;
import com.alibaba.chaosblade.exec.common.model.action.ActionExecutor;
import com.alibaba.chaosblade.exec.common.model.action.ActionSpec;
import com.alibaba.chaosblade.exec.common.model.action.DirectlyInjectionAction;
import com.alibaba.chaosblade.exec.common.model.handler.PreCreateInjectionModelHandler;
import com.alibaba.chaosblade.exec.common.model.handler.PreDestroyInjectionModelHandler;
import com.alibaba.chaosblade.exec.common.plugin.MethodModelSpec;
import com.alibaba.chaosblade.exec.plugin.jvm.codecache.CodeCacheFillingActionSpec;
import com.alibaba.chaosblade.exec.plugin.jvm.cpu.JvmCpuFullLoadActionSpec;
import com.alibaba.chaosblade.exec.plugin.jvm.gc.FullGCActionSpec;
import com.alibaba.chaosblade.exec.plugin.jvm.oom.JvmOomActionSpec;
import com.alibaba.chaosblade.exec.plugin.jvm.script.model.JvmDynamicActionSpec;
import com.alibaba.chaosblade.exec.plugin.jvm.thread.model.JvmThreadFullActionSpec;
import java.util.Arrays;

/**
 * Jvm model spec
 *
 * <p>The jvm model plugin is used for creating common java fault injections.
 *
 * <p>For example
 *
 * <ul>
 *   <li>inject any java methods
 *   <li>cause jvm oom
 * </ul>
 *
 * @author RinaisSuper
 * @date 2019-04-19
 */
public class JvmModelSpec extends MethodModelSpec
    implements PreCreateInjectionModelHandler, PreDestroyInjectionModelHandler {

  public JvmModelSpec() {
    super();
    addActionSpec(new JvmOomActionSpec());
    addActionSpec(new JvmCpuFullLoadActionSpec());
    addActionSpec(new JvmDynamicActionSpec());
    addActionSpec(new CodeCacheFillingActionSpec());
    addActionSpec(new JvmThreadFullActionSpec());
    addActionSpec(new FullGCActionSpec());
  }

  @Override
  public void preDestroy(String uid, Model model) throws ExperimentException {
    ActionSpec actionSpec = getActionSpec(model.getActionName());
    if (actionSpec instanceof DirectlyInjectionAction) {
      try {
        ((DirectlyInjectionAction) actionSpec).destroyInjection(uid, model);
      } catch (Exception e) {
        throw new ExperimentException("destroy injection failed:" + e.getMessage());
      }
    } else {
      // invoke action executor stop method if the executor extends StoppableActionExecutor class
      ActionExecutor actionExecutor = actionSpec.getActionExecutor();
      if (actionExecutor instanceof StoppableActionExecutor) {
        EnhancerModel enhancerModel = new EnhancerModel(null, model.getMatcher());
        try {
          ((StoppableActionExecutor) actionExecutor).stop(enhancerModel);
        } catch (Exception e) {
          throw new ExperimentException("stop experiment exception", e);
        }
      }
      MethodPreInjectHandler.preHandleRecovery(model);
    }
  }

  @Override
  public void preCreate(String uid, Model model) throws ExperimentException {
    ActionSpec actionSpec = getActionSpec(model.getActionName());
    if (actionSpec instanceof DirectlyInjectionAction) {
      try {
        ((DirectlyInjectionAction) actionSpec).createInjection(uid, model);
      } catch (Exception e) {
        throw new ExperimentException(
            "create injection failed: " + Arrays.toString(e.getStackTrace()) + ", " + e, e);
      }
    } else {
      MethodPreInjectHandler.preHandleInjection(model);
    }
  }
}
