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

package com.alibaba.chaosblade.exec.plugin.log.log4j2;

import com.alibaba.chaosblade.exec.common.aop.BeforeEnhancer;
import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherModel;
import com.alibaba.chaosblade.exec.plugin.log.LogConstant;
import java.lang.reflect.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** @author orion233 */
public class Log4j2Enhancer extends BeforeEnhancer {
  private static final Logger LOGGER = LoggerFactory.getLogger(Log4j2Enhancer.class);

  @Override
  public EnhancerModel doBeforeAdvice(
      ClassLoader classLoader,
      String className,
      Object object,
      Method method,
      Object[] methodArguments)
      throws Exception {
    LOGGER.info(
        "log4j2 do before, classLoader:{}, className:{}, object:{}, method:{}, args:{}",
        classLoader,
        className,
        object,
        method.getName(),
        methodArguments);

    EnhancerModel enhancerModel = new EnhancerModel(classLoader, new MatcherModel());
    enhancerModel.addMatcher(LogConstant.LOG4J2_KEY, "true");
    return enhancerModel;
  }
}
