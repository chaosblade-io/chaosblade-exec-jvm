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

package com.otel.business.c.exec.plugin.jvm;

import com.otel.business.c.exec.common.aop.PluginBean;
import com.otel.business.c.exec.common.aop.PluginLifecycleListener;
import com.otel.business.c.exec.common.aop.PointCut;
import com.otel.business.c.exec.common.aop.matcher.clazz.ClassMatcher;
import com.otel.business.c.exec.common.aop.matcher.clazz.NameClassMatcher;
import com.otel.business.c.exec.common.aop.matcher.method.MethodMatcher;
import com.otel.business.c.exec.common.aop.matcher.method.NameMethodMatcher;
import com.otel.business.c.exec.common.center.ManagerFactory;
import com.otel.business.c.exec.common.exception.ExperimentException;
import com.otel.business.c.exec.common.model.Model;
import com.otel.business.c.exec.common.model.matcher.MatcherModel;
import com.otel.business.c.exec.common.plugin.MethodConstant;
import com.otel.business.c.exec.common.plugin.MethodPlugin;

/** @author changjun.xcj */
public class MethodPreInjectHandler {

  /**
   * Add class and method listeners
   *
   * @param model
   * @throws ExperimentException
   */
  public static void preHandleInjection(Model model) throws ExperimentException {
    MethodPlugin methodPlugin = createMethodPlugin(model);
    PluginLifecycleListener listener = getPluginLifecycleListener();
    listener.add(new PluginBean(methodPlugin));
  }

  public static void preHandleRecovery(Model model) throws ExperimentException {
    MethodPlugin methodPlugin = createMethodPlugin(model);
    PluginLifecycleListener listener = getPluginLifecycleListener();
    listener.delete(new PluginBean(methodPlugin));
  }

  private static PluginLifecycleListener getPluginLifecycleListener() throws ExperimentException {
    PluginLifecycleListener listener =
        ManagerFactory.getListenerManager().getPluginLifecycleListener();
    if (listener == null) {
      throw new ExperimentException("can get plugin listener");
    }
    return listener;
  }

  private static MethodPlugin createMethodPlugin(Model model) {
    MatcherModel matcher = model.getMatcher();
    final String className = matcher.get(MethodConstant.CLASS_MATCHER_NAME);
    final String methodName = matcher.get(MethodConstant.METHOD_MATCHER_NAME);
    final String afterFlag = matcher.get(MethodConstant.AFTER_METHOD_FLAG);
    PointCut pointCut =
        new PointCut() {
          @Override
          public ClassMatcher getClassMatcher() {
            return new NameClassMatcher(className);
          }

          @Override
          public MethodMatcher getMethodMatcher() {
            return new NameMethodMatcher(methodName);
          }
        };
    String pluginName = className + "#" + methodName;
    boolean isAfterEvent = false;
    if (Boolean.valueOf(afterFlag).equals(Boolean.TRUE)) {
      isAfterEvent = true;
    }
    return new MethodPlugin(pluginName, pointCut, isAfterEvent);
  }
}
