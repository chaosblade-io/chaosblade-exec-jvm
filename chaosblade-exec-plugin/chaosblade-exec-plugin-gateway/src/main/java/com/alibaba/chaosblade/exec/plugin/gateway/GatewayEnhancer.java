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

package com.alibaba.chaosblade.exec.plugin.gateway;

import com.alibaba.chaosblade.exec.common.aop.BeforeEnhancer;
import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherModel;
import com.alibaba.chaosblade.exec.common.util.JsonUtil;
import com.alibaba.chaosblade.exec.common.util.ReflectUtil;
import java.lang.reflect.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author wb-shd671576
 *
 * @package: com.alibaba.chaosblade.exec.plugin.gateway @Date 2021-07-29
 */
public class GatewayEnhancer extends BeforeEnhancer {

  private static final Logger LOGGER = LoggerFactory.getLogger(GatewayEnhancer.class);

  @Override
  public EnhancerModel doBeforeAdvice(
      ClassLoader classLoader,
      String className,
      Object object,
      Method method,
      Object[] methodArguments)
      throws Exception {
    if (methodArguments == null || object == null) {
      LOGGER.warn("The necessary parameters is null");
      return null;
    }
    Object serverWebExchange = methodArguments[0];
    Object request =
        ReflectUtil.invokeMethod(serverWebExchange, "getRequest", new Object[] {}, false);
    MatcherModel matcherModel = new MatcherModel();
    if (request != null) {
      Object url = ReflectUtil.invokeMethod(request, "getURI", new Object[] {}, false);
      if (url != null) {
        String path =
            ReflectUtil.invokeMethod(request, "getPath", new Object[] {}, false).toString();
        matcherModel.add(GatewayConstant.GET_REQUST_PATH, path);
      }
    }
    LOGGER.debug("Gateway matchers: {}", JsonUtil.writer().writeValueAsString(matcherModel));
    return new EnhancerModel(classLoader, matcherModel);
  }
}
