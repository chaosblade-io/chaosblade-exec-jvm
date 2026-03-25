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

package com.alibaba.chaosblade.exec.plugin.http.httpclient5;

import static com.alibaba.chaosblade.exec.plugin.http.HttpConstant.DEFAULT_TIMEOUT;
import static com.alibaba.chaosblade.exec.plugin.http.HttpConstant.HTTPCLIENT5;
import static com.alibaba.chaosblade.exec.plugin.http.HttpConstant.TARGET_NAME;
import static com.alibaba.chaosblade.exec.plugin.http.HttpConstant.URI_KEY;
import static com.alibaba.chaosblade.exec.plugin.http.HttpConstant.methodMap;

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

/** HttpClient5 enhancer implementation. */
public class HttpClient5Enhancer extends HttpEnhancer {

  private static final Logger LOGGER = LoggerFactory.getLogger(HttpClient5Enhancer.class);

  private static final String GET_CONFIG = "getConfig";
  private static final String GET_CONNECT_TIMEOUT = "getConnectTimeout";
  private static final String GET_RESPONSE_TIMEOUT = "getResponseTimeout";
  private static final String GET_FIRST_HEADER = "getFirstHeader";
  private static final String GET_URI = "getUri";
  private static final String GET_REQUEST_URI = "getRequestUri";
  private static final String GET_VALUE = "getValue";
  private static final String TO_MILLISECONDS = "toMilliseconds";

  @Override
  protected void postDoBeforeAdvice(EnhancerModel enhancerModel) {
    enhancerModel.addMatcher(HTTPCLIENT5, "true");
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
            Object request = getRequest(methodArguments);
            Object header =
                ReflectUtil.invokeMethod(request, GET_FIRST_HEADER, new Object[] {key}, false);
            return (String) ReflectUtil.invokeMethod(header, GET_VALUE, new Object[0], false);
          }
        });
  }

  @Override
  protected int getTimeout(Object instance, Object[] methodArguments) {
    try {
      Object config = null;
      Object request = getRequest(methodArguments);
      if (request != null) {
        config = ReflectUtil.invokeMethod(request, GET_CONFIG, new Object[0], false);
      }
      if (config == null) {
        config = ReflectUtil.invokeMethod(instance, GET_CONFIG, new Object[0], false);
      }
      if (config == null) {
        LOGGER.warn(
            "RequestConfig from HttpClient5 not found. return default value {}", DEFAULT_TIMEOUT);
        return DEFAULT_TIMEOUT;
      }
      int connectionTimeout =
          timeoutToMilliseconds(
              ReflectUtil.invokeMethod(config, GET_CONNECT_TIMEOUT, new Object[0], false));
      int responseTimeout =
          timeoutToMilliseconds(
              ReflectUtil.invokeMethod(config, GET_RESPONSE_TIMEOUT, new Object[0], false));
      if (connectionTimeout + responseTimeout <= 0) {
        LOGGER.warn("timeout did not config. return default value {}", DEFAULT_TIMEOUT);
        return DEFAULT_TIMEOUT;
      }
      return connectionTimeout + responseTimeout;
    } catch (Exception e) {
      LOGGER.warn(
          "Getting timeout from HttpClient5 occurs exception. return default value {}",
          DEFAULT_TIMEOUT,
          e);
      return DEFAULT_TIMEOUT;
    }
  }

  @Override
  protected String getUrl(Object instance, Object[] object) throws Exception {
    Object request = getRequest(object);
    if (request == null) {
      LOGGER.warn("HttpClient5 request is null, can not get necessary values.");
      return null;
    }
    Method method = methodMap.get(getMethodName());
    if (method == null) {
      method = request.getClass().getMethod(GET_URI);
      methodMap.put(getMethodName(), method);
    }
    Object uri = method.invoke(request);
    if (uri == null) {
      uri = ReflectUtil.invokeMethod(request, GET_REQUEST_URI, new Object[0], false);
    }
    if (uri == null) {
      LOGGER.warn("HttpClient5 request uri is null, can not get necessary values.");
      return null;
    }
    return UrlUtils.getUrlExcludeQueryParameters(uri.toString());
  }

  private Object getRequest(Object[] methodArguments) {
    if (methodArguments == null || methodArguments.length < 2) {
      return null;
    }
    return methodArguments[1];
  }

  private int timeoutToMilliseconds(Object timeout) throws Exception {
    if (timeout == null) {
      return 0;
    }
    Long milliseconds = ReflectUtil.invokeMethod(timeout, TO_MILLISECONDS, new Object[0], false);
    if (milliseconds == null || milliseconds.longValue() <= 0L) {
      return 0;
    }
    if (milliseconds.longValue() > Integer.MAX_VALUE) {
      return Integer.MAX_VALUE;
    }
    return milliseconds.intValue();
  }

  private String getMethodName() {
    return HTTPCLIENT5 + GET_URI + URI_KEY;
  }
}
