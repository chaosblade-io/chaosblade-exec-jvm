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

package com.alibaba.chaosblade.exec.common.aop;

import com.alibaba.chaosblade.exec.common.center.ManagerFactory;
import com.alibaba.chaosblade.exec.common.injection.Injector;
import java.lang.reflect.Method;

/** @author Changjun Xiao */
public abstract class BeforeEnhancer implements Enhancer {

  /**
   * Do fault-inject
   *
   * @param targetName the plugin target name
   * @param classLoader classloader for the class
   * @param className the class name
   * @param object the class instance. Value is null if the method is static
   * @param method the class method
   * @param methodArguments the method arguments
   * @throws Exception
   */
  @Override
  public void beforeAdvice(
      String targetName,
      ClassLoader classLoader,
      String className,
      Object object,
      Method method,
      Object[] methodArguments)
      throws Exception {
    if (!ManagerFactory.getStatusManager().expExists(targetName)) {
      return;
    }
    EnhancerModel model = doBeforeAdvice(classLoader, className, object, method, methodArguments);
    if (model == null) {
      return;
    }
    model
        .setTarget(targetName)
        .setMethod(method)
        .setObject(object)
        .setMethodArguments(methodArguments);
    Injector.inject(model);
  }

  /**
   * Do handle enhancer
   *
   * @param classLoader
   * @param className
   * @param object
   * @param method
   * @param methodArguments
   * @return enhancer model to match the experiment rule
   * @throws Exception
   */
  public abstract EnhancerModel doBeforeAdvice(
      ClassLoader classLoader,
      String className,
      Object object,
      Method method,
      Object[] methodArguments)
      throws Exception;

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
    return;
  }
}
