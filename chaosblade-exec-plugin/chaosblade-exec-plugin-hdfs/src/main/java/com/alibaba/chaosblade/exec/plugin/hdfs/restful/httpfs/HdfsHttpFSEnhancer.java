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

package com.alibaba.chaosblade.exec.plugin.hdfs.restful.httpfs;

import com.alibaba.chaosblade.exec.common.aop.BeforeEnhancer;
import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherModel;
import com.alibaba.chaosblade.exec.common.util.JsonUtil;
import com.alibaba.chaosblade.exec.common.util.StringUtils;
import com.alibaba.chaosblade.exec.plugin.hdfs.common.HdfsConstant;
import java.lang.reflect.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HdfsHttpFSEnhancer extends BeforeEnhancer {
  private static final Logger LOGGER = LoggerFactory.getLogger(HdfsHttpFSEnhancer.class);

  @Override
  public EnhancerModel doBeforeAdvice(
      ClassLoader classLoader,
      String className,
      Object object,
      Method method,
      Object[] methodArguments)
      throws Exception {
    MatcherModel matcherModel = new MatcherModel();
    if (HdfsHttpFSPointCut.METHOD_HDFS_HTTPFS_GET.equals(method.getName())
        || HdfsHttpFSPointCut.METHOD_HDFS_HTTPFS_DELETE.equals(method.getName())) {
      if (StringUtils.isNotBlank(String.valueOf(methodArguments[0]))) {
        matcherModel.add(
            HdfsConstant.FLAG_COMMON_FILE, HdfsConstant.HDFS_ROOT_PATH + methodArguments[0]);
      }
      if (methodArguments[1] != null
          && StringUtils.isNotBlank(String.valueOf(methodArguments[1]))) {
        matcherModel.add(
            HdfsConstant.FLAG_RESTFUL_OPERATION, String.valueOf(methodArguments[1]).toLowerCase());
      }
    }
    if (HdfsHttpFSPointCut.METHOD_HDFS_HTTPFS_PUT.equals(method.getName())
        || HdfsHttpFSPointCut.METHOD_HDFS_HTTPFS_POST.equals(method.getName())) {
      if (StringUtils.isNotBlank(String.valueOf(methodArguments[2]))) {
        matcherModel.add(
            HdfsConstant.FLAG_COMMON_FILE, HdfsConstant.HDFS_ROOT_PATH + methodArguments[2]);
      }
      if (methodArguments[1] != null
          && StringUtils.isNotBlank(String.valueOf(methodArguments[3]))) {
        matcherModel.add(
            HdfsConstant.FLAG_RESTFUL_OPERATION, String.valueOf(methodArguments[3]).toLowerCase());
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
