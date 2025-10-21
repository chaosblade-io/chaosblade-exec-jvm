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

import java.lang.reflect.Method;

/** @author Changjun Xiao */
public interface Enhancer {

  /**
   * Enhanced processing before method execution
   *
   * @param targetName
   * @param classLoader
   * @param className
   * @param object
   * @param method
   * @param methodArguments
   * @throws Exception
   */
  void beforeAdvice(
      String targetName,
      ClassLoader classLoader,
      String className,
      Object object,
      Method method,
      Object[] methodArguments)
      throws Exception;

  /**
   * Enhanced processing after method execution
   *
   * @param targetName
   * @param classLoader
   * @param className
   * @param object
   * @param method
   * @param methodArguments
   * @param returnObject
   */
  void afterAdvice(
      String targetName,
      ClassLoader classLoader,
      String className,
      Object object,
      Method method,
      Object[] methodArguments,
      Object returnObject)
      throws Exception;
}
