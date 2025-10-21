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

package com.alibaba.chaosblade.exec.plugin.http.httpclient3;

import static com.alibaba.chaosblade.exec.plugin.http.HttpConstant.*;

import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.common.util.BusinessParamUtil;
import com.alibaba.chaosblade.exec.common.util.ReflectUtil;
import com.alibaba.chaosblade.exec.plugin.http.HttpEnhancer;
import com.alibaba.chaosblade.exec.plugin.http.UrlUtils;
import com.alibaba.chaosblade.exec.spi.BusinessDataGetter;
import java.lang.reflect.Method;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author yuhan
 *
 * @package: com.alibaba.chaosblade.exec.plugin.restTemplate @Date 2019-05-08 20:23
 */
public class HttpClient3Enhancer extends HttpEnhancer {
  private static final Logger LOGGER = LoggerFactory.getLogger(HttpClient3Enhancer.class);
  private static final String GET_HTTP_CONNECTION_MANAGER = "getHttpConnectionManager";
  private static final String GET_PARAMS = "getParams";
  private static final String GET_SOCKET_TIMEOUT = "getSoTimeout";
  private static final String GET_CONNECTION_TIMEOUT = "getConnectionTimeout";
  private static final String GET_REQUEST_HEADER = "getRequestHeader";
  private static final String GET_VALUE = "getValue";

  @Override
  protected void postDoBeforeAdvice(EnhancerModel enhancerModel) {
    enhancerModel.addMatcher(HTTPCLIENT3, "true");
  }

  @Override
  protected Map<String, Map<String, String>> getBusinessParams(
      String className, Object instance, Method method, final Object[] methodArguments)
      throws Exception {
    return BusinessParamUtil.getAndParse(
        TARGET_NAME,
        new BusinessDataGetter() {
          @Override
          public String get(String key) throws Exception {
            Object httpMethod = methodArguments[1];
            Object header =
                ReflectUtil.invokeMethod(httpMethod, GET_REQUEST_HEADER, new Object[] {key}, false);
            return (String) ReflectUtil.invokeMethod(header, GET_VALUE, new Object[0], false);
          }
        });
  }

  @Override
  protected int getTimeout(Object instance, Object[] methodArguments) {
    try {
      Object manager =
          ReflectUtil.invokeMethod(instance, GET_HTTP_CONNECTION_MANAGER, new Object[0], false);
      if (manager == null) {
        LOGGER.warn(
            "HttpConnectionManager from HttpClient not found. return default value {}",
            DEFAULT_TIMEOUT);
        return DEFAULT_TIMEOUT;
      }

      Object params = ReflectUtil.invokeMethod(manager, GET_PARAMS, new Object[0], false);
      if (params == null) {
        LOGGER.warn(
            "HttpConnectionManagerParams from HttpConnectionManager not found. return default value {}",
            DEFAULT_TIMEOUT);
        return DEFAULT_TIMEOUT;
      }
      int connectionTimeout =
          ReflectUtil.invokeMethod(params, GET_CONNECTION_TIMEOUT, new Object[0], false);
      int socketTimeout =
          ReflectUtil.invokeMethod(params, GET_SOCKET_TIMEOUT, new Object[0], false);
      return connectionTimeout + socketTimeout;
    } catch (Exception e) {
      LOGGER.warn(
          "Getting timeout from url occurs exception. return default value " + DEFAULT_TIMEOUT, e);
    }
    return DEFAULT_TIMEOUT;
  }

  @Override
  protected String getUrl(Object instance, Object[] object) throws Exception {
    Object httpMethod = object[1];
    Method method = methodMap.get(getMethodName());
    if (null == method) {
      method = object[1].getClass().getMethod(getURI, null);
      methodMap.put(getMethodName(), method);
    }
    if (null != method) {
      Object invoke = method.invoke(httpMethod, null);
      if (null != invoke) {
        return UrlUtils.getUrlExcludeQueryParameters(invoke.toString());
      }
    }
    return null;
  }

  private String getMethodName() {
    return HTTPCLIENT3 + getURI;
  }
}
