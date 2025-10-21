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

package com.alibaba.chaosblade.exec.common.plugin;

import com.alibaba.chaosblade.exec.common.aop.AfterEnhancer;
import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.common.constant.ModelConstant;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherModel;
import java.lang.reflect.Method;

/** @author Changjun Xiao */
public class MethodEnhancerForAfter extends AfterEnhancer {

  @Override
  public EnhancerModel doAfterAdvice(
      ClassLoader classLoader,
      String className,
      Object object,
      Method method,
      Object[] methodArguments,
      Object returnObject)
      throws Exception {
    MatcherModel matcherModel = new MatcherModel();
    matcherModel.add(MethodConstant.CLASS_MATCHER_NAME, className);
    matcherModel.add(MethodConstant.METHOD_MATCHER_NAME, method.getName());
    matcherModel.add(MethodConstant.AFTER_METHOD_FLAG, "true");
    EnhancerModel model = new EnhancerModel(classLoader, matcherModel);
    model
        .setTarget(ModelConstant.JVM_TARGET)
        .setClassLoader(classLoader)
        .setObject(object)
        .setMethod(method)
        .setMethodArguments(methodArguments)
        .setReturnValue(returnObject);
    return model;
  }
}
