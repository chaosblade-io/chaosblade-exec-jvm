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

package com.alibaba.chaosblade.exec.plugin.security;

import com.alibaba.chaosblade.exec.common.aop.BeforeEnhancer;
import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherModel;
import java.lang.reflect.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** @author liubin@njzfit.cn */
public class SecurityEnhancer extends BeforeEnhancer {

  private static final Logger LOGGER = LoggerFactory.getLogger(SecurityEnhancer.class);

  @Override
  public EnhancerModel doBeforeAdvice(
      ClassLoader classLoader,
      String className,
      Object object,
      Method method,
      Object[] methodArguments)
      throws Exception {
    if (methodArguments.length < 1) {
      LOGGER.warn(
          "argument's length less than 1, className:{}, methodName:{}",
          className,
          method.getName());
      return null;
    }
    Object username = methodArguments[0];

    MatcherModel matcherModel = new MatcherModel();
    matcherModel.add(SecurityConstant.PARAM_USERNAME, username);
    return new EnhancerModel(classLoader, matcherModel);
  }
}
