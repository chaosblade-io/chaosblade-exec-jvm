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

package com.alibaba.chaosblade.exec.plugin.zookeeper;

import com.alibaba.chaosblade.exec.common.aop.BeforeEnhancer;
import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherModel;
import com.alibaba.chaosblade.exec.common.util.JsonUtil;
import com.alibaba.chaosblade.exec.common.util.ReflectUtil;
import java.lang.reflect.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** @author liuhq @Date 2020/11/23 上午11:40 */
public class ZookeeperEnhancer extends BeforeEnhancer {

  private static final Logger LOGGER = LoggerFactory.getLogger(ZookeeperEnhancer.class);

  @Override
  public EnhancerModel doBeforeAdvice(
      ClassLoader classLoader,
      String className,
      Object object,
      Method method,
      Object[] methodArguments)
      throws Exception {

    if (methodArguments == null || methodArguments.length == 0) {
      LOGGER.info(
          "The necessary parameters is null or length is not equal 1, {}",
          methodArguments != null ? methodArguments.length : null);
      return null;
    }
    Object packgeParam = null;

    if (method.getName().equals(ZookeeperConstant.ZK_SEND_METHOD)) {
      packgeParam = methodArguments[0];
    } else if (method.getName().equals(ZookeeperConstant.ZK_SUBMIT_METHOD)) {
      packgeParam = methodArguments[1];
    }
    if (packgeParam == null) {
      return null;
    }
    boolean packgeClassFlag =
        ReflectUtil.isAssignableFrom(
            classLoader, packgeParam.getClass(), ZookeeperConstant.PARAM_CLASS_NAME);
    String path = null;
    if (packgeClassFlag) {
      try {
        path = ReflectUtil.invokeMethod(packgeParam, "getPath", new Object[0], false);
      } catch (Exception e) {
        LOGGER.info("not find method");
        return null;
      }
    }
    if (path == null) {
      return null;
    }

    MatcherModel matcherModel = new MatcherModel();

    matcherModel.add(ZookeeperConstant.PATH_MATCHER_NAME, path.toLowerCase());
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("zk matchers: {}", JsonUtil.writer().writeValueAsString(matcherModel));
    }
    return new EnhancerModel(classLoader, matcherModel);
  }
}
