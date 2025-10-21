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

package com.alibaba.chaosblade.exec.plugin.tars;

import com.alibaba.chaosblade.exec.common.aop.BeforeEnhancer;
import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.common.model.action.delay.TimeoutExecutor;
import java.lang.reflect.Method;

/**
 * @author saikei
 * @email lishiji@huya.com
 */
public abstract class TarsEnhancer extends BeforeEnhancer {

  /**
   * doBeforeAdvice
   *
   * @param classLoader
   * @param className
   * @param object
   * @param method
   * @param methodArguments
   * @return
   * @throws Exception
   */
  @Override
  public abstract EnhancerModel doBeforeAdvice(
      ClassLoader classLoader,
      String className,
      Object object,
      Method method,
      Object[] methodArguments)
      throws Exception;

  /**
   * Create timeout executor
   *
   * @param classLoader
   * @param timeout
   * @param className
   * @return
   */
  protected abstract TimeoutExecutor createTimeoutExecutor(
      ClassLoader classLoader, long timeout, String className);
}
