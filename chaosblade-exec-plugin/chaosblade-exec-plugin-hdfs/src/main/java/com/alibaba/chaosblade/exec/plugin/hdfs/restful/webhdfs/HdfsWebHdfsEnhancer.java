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

package com.alibaba.chaosblade.exec.plugin.hdfs.restful.webhdfs;

import com.alibaba.chaosblade.exec.common.aop.BeforeEnhancer;
import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherModel;
import com.alibaba.chaosblade.exec.common.util.JsonUtil;
import com.alibaba.chaosblade.exec.common.util.ReflectUtil;
import com.alibaba.chaosblade.exec.common.util.StringUtils;
import com.alibaba.chaosblade.exec.plugin.hdfs.common.HdfsConstant;
import java.lang.reflect.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HdfsWebHdfsEnhancer extends BeforeEnhancer {
  private static final Logger LOGGER = LoggerFactory.getLogger(HdfsWebHdfsEnhancer.class);

  @Override
  public EnhancerModel doBeforeAdvice(
      ClassLoader classLoader,
      String className,
      Object object,
      Method method,
      Object[] methodArguments)
      throws Exception {
    MatcherModel matcherModel = new MatcherModel();
    if (methodArguments[4] != null) {
      String filePath = String.valueOf(methodArguments[4]);
      if (StringUtils.isNotBlank(filePath)) {
        matcherModel.add(HdfsConstant.FLAG_COMMON_FILE, filePath.trim());
      }
    }
    if (methodArguments[5] != null) {
      Object operationParam = ReflectUtil.invokeMethod(methodArguments[5], "getValue");
      if (operationParam != null) {
        String operation = ReflectUtil.invokeMethod(operationParam, "name");
        if (StringUtils.isNotBlank(operation)) {
          matcherModel.add(HdfsConstant.FLAG_RESTFUL_OPERATION, operation.trim().toLowerCase());
        }
      }
    }

    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug(
          "{} matchers: {}",
          getClass().getName(),
          JsonUtil.writer().writeValueAsString(matcherModel));
    }

    return new EnhancerModel(classLoader, matcherModel);
  }
}
