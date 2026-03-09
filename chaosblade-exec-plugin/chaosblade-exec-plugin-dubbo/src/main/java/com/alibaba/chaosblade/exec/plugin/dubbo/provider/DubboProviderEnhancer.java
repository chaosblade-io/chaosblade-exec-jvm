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

package com.alibaba.chaosblade.exec.plugin.dubbo.provider;

import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.common.model.action.delay.BaseTimeoutExecutor;
import com.alibaba.chaosblade.exec.common.model.action.delay.TimeoutExecutor;
import com.alibaba.chaosblade.exec.common.util.BusinessParamUtil;
import com.alibaba.chaosblade.exec.common.util.ReflectUtil;
import com.alibaba.chaosblade.exec.plugin.dubbo.DubboConstant;
import com.alibaba.chaosblade.exec.plugin.dubbo.DubboEnhancer;
import com.alibaba.chaosblade.exec.spi.BusinessDataGetter;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** @author Changjun Xiao */
public class DubboProviderEnhancer extends DubboEnhancer {

  private static final Logger LOGGER = LoggerFactory.getLogger(DubboProviderEnhancer.class);
  private static final String GET_METHOD_PARAMETER = "getMethodParameter";
  private static final String TIMEOUT_KEY = "timeout";
  private static final int DEFAULT_TIMEOUT = 0;

  @Override
  protected Object getUrl(Object instance, Object invocation) throws Exception {
    Object invoker = ReflectUtil.invokeMethod(invocation, GET_INVOKER, new Object[0], false);
    return ReflectUtil.invokeMethod(invoker, GET_URL, new Object[0], false);
  }

  @Override
  protected TimeoutExecutor createTimeoutExecutor(
      ClassLoader classLoader, long timeout, String className) {
    if (timeout <= 0) {
      return null;
    }
    return new BaseTimeoutExecutor(classLoader, timeout) {
      @Override
      public Exception generateTimeoutException(ClassLoader classLoader) {
        return new RuntimeException(DubboConstant.TIMEOUT_EXCEPTION_MSG);
      }
    };
  }

  @Override
  protected Map<String, Map<String, String>> getBusinessParams(final Object invocation)
      throws Exception {
    return BusinessParamUtil.getAndParse(
        DubboConstant.TARGET_NAME,
        new BusinessDataGetter() {
          @Override
          public String get(String key) throws Exception {
            Map<String, String> attachments =
                ReflectUtil.invokeMethod(invocation, GET_ATTACHMENTS, new Object[0], false);
            if (attachments == null || attachments.isEmpty()) {
              return null;
            }
            return attachments.get(key);
          }
        });
  }

  @Override
  protected void postDoBeforeAdvice(EnhancerModel enhancerModel) {
    enhancerModel.addMatcher(DubboConstant.PROVIDER_KEY, "true");
  }

  @Override
  protected int getTimeout(String method, Object instance, Object invocation) {
    try {
      Object invoker = ReflectUtil.invokeMethod(invocation, GET_INVOKER, new Object[0], false);
      Object url = ReflectUtil.invokeMethod(invoker, GET_URL, new Object[0], false);
      if (url != null) {
        Integer timeout =
            ReflectUtil.invokeMethod(
                url,
                GET_METHOD_PARAMETER,
                new Object[] {method, TIMEOUT_KEY, DEFAULT_TIMEOUT},
                false);
        return timeout != null ? timeout : DEFAULT_TIMEOUT;
      }
    } catch (Exception e) {
      LOGGER.warn("Getting timeout from url occurs exception for provider", e);
    }
    return DEFAULT_TIMEOUT;
  }
}
