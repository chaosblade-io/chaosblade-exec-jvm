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
import com.alibaba.chaosblade.exec.common.aop.BeforeEnhancer;
import com.alibaba.chaosblade.exec.common.aop.Enhancer;
import java.lang.reflect.Method;

/** @author Changjun Xiao */
public class MethodEnhancer implements Enhancer {

  private BeforeEnhancer beforeEnhancer;
  private AfterEnhancer afterEnhancer;

  public MethodEnhancer() {
    beforeEnhancer = new MethodEnhancerForBefore();
    afterEnhancer = new MethodEnhancerForAfter();
  }

  @Override
  public void beforeAdvice(
      String targetName,
      ClassLoader classLoader,
      String className,
      Object object,
      Method method,
      Object[] methodArguments)
      throws Exception {
    beforeEnhancer.beforeAdvice(
        targetName, classLoader, className, object, method, methodArguments);
  }

  @Override
  public void afterAdvice(
      String targetName,
      ClassLoader classLoader,
      String className,
      Object object,
      Method method,
      Object[] methodArguments,
      Object returnObject)
      throws Exception {
    afterEnhancer.afterAdvice(
        targetName, classLoader, className, object, method, methodArguments, returnObject);
  }
}
