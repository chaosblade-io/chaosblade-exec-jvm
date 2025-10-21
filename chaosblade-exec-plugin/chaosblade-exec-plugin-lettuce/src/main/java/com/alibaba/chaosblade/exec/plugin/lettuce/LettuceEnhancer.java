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

package com.alibaba.chaosblade.exec.plugin.lettuce;

import static com.alibaba.chaosblade.exec.plugin.lettuce.LettuceConstants.CMD;
import static com.alibaba.chaosblade.exec.plugin.lettuce.LettuceConstants.KEY;

import com.alibaba.chaosblade.exec.common.aop.BeforeEnhancer;
import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherModel;
import com.alibaba.chaosblade.exec.common.util.JsonUtil;
import com.alibaba.chaosblade.exec.common.util.ReflectUtil;
import java.lang.reflect.Method;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** @author yefei */
public class LettuceEnhancer extends BeforeEnhancer {

  private static final Logger logger = LoggerFactory.getLogger(LettuceEnhancer.class);

  @Override
  public EnhancerModel doBeforeAdvice(
      ClassLoader classLoader,
      String className,
      Object object,
      Method method,
      Object[] methodArguments)
      throws Exception {
    Object command = methodArguments[1];
    Object args = ReflectUtil.getFieldValue(command, "command", false);
    Object commandType = ReflectUtil.getFieldValue(args, "type", false);

    Object commandArgs = ReflectUtil.getFieldValue(args, "args", false);
    List singularArguments = ReflectUtil.getFieldValue(commandArgs, "singularArguments", false);
    Object keyArgument = singularArguments.get(0);
    MatcherModel matcherModel = new MatcherModel();
    if (keyArgument == null) {
      return null;
    }
    Object key = ReflectUtil.getFieldValue(keyArgument, "key", false);
    matcherModel.add(KEY, key);
    matcherModel.add(CMD, commandType);
    logger.debug("lettuce matchers: {}", JsonUtil.writer().writeValueAsString(matcherModel));
    return new EnhancerModel(classLoader, matcherModel);
  }
}
