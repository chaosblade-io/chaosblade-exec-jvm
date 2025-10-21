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

package com.alibaba.chaosblade.exec.plugin.http.asynchttpclient;

import static com.alibaba.chaosblade.exec.plugin.http.HttpConstant.DEFAULT_TIMEOUT;

import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.common.context.ThreadLocalContext;
import com.alibaba.chaosblade.exec.common.model.action.delay.TimeoutExecutor;
import com.alibaba.chaosblade.exec.common.util.FlagUtil;
import com.alibaba.chaosblade.exec.common.util.ReflectUtil;
import com.alibaba.chaosblade.exec.plugin.http.HttpConstant;
import com.alibaba.chaosblade.exec.plugin.http.HttpEnhancer;
import com.alibaba.chaosblade.exec.plugin.http.UrlUtils;
import com.alibaba.chaosblade.exec.plugin.http.enhancer.InternalPointCut;
import com.alibaba.chaosblade.exec.plugin.http.model.CallPointMatcher;
import java.lang.reflect.Method;
import java.net.SocketTimeoutException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** @author shizhi.zhu@qunar.com */
@InternalPointCut(
    className = "com.ning.http.client.AsyncCompletionHandler",
    methodName = "onStatusReceived")
public class AsyncHttpClientHandlerEnhancer extends HttpEnhancer {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(AsyncHttpClientHandlerEnhancer.class);
  private static final String GET_CONFIG = "getConfig";
  private static final String GET_CONNECTION_TIMEOUT = "getConnectTimeout";
  private static final String GET_READ_TIMEOUT = "getReadTimeout";

  @Override
  protected void postDoBeforeAdvice(EnhancerModel enhancerModel) {
    ThreadLocalContext.Content content = ThreadLocalContext.getInstance().get();
    enhancerModel.addMatcher(HttpConstant.ASYNC_HTTP_TARGET_NAME, "true");
    if (FlagUtil.hasFlag("http", HttpConstant.CALL_POINT_KEY)) {
      enhancerModel.addCustomMatcher(
          HttpConstant.CALL_POINT_KEY,
          content.getStackTraceElements(),
          CallPointMatcher.getInstance());
    }
  }

  @Override
  protected Map<String, Map<String, String>> getBusinessParams(
      String className, Object instance, Method method, Object[] methodArguments) throws Exception {
    ThreadLocalContext.Content content = ThreadLocalContext.getInstance().get();
    if (content == null) {
      return null;
    }
    return content.getBusinessData();
  }

  @Override
  protected boolean shouldAddCallPoint() {
    return false;
  }

  @Override
  protected int getTimeout(Object instance, Object[] methodArguments) {
    try {
      if (methodArguments.length < 1) {
        LOGGER.warn("HttpResponseStatus not found. return default value {}", DEFAULT_TIMEOUT);
        return DEFAULT_TIMEOUT;
      }
      Object status = methodArguments[0];
      Object config = ReflectUtil.invokeMethod(status, GET_CONFIG, new Object[0], false);
      if (config == null) {
        LOGGER.warn(
            "AsyncHttpClientConfig from status not found. return default value {}",
            DEFAULT_TIMEOUT);
        return DEFAULT_TIMEOUT;
      }
      int connectionTimeout =
          ReflectUtil.invokeMethod(config, GET_CONNECTION_TIMEOUT, new Object[0], false);
      int readTimeout = ReflectUtil.invokeMethod(config, GET_READ_TIMEOUT, new Object[0], false);
      return connectionTimeout + readTimeout;
    } catch (Exception e) {
      LOGGER.warn(
          "Getting timeout from url occurs exception. return default value {}", DEFAULT_TIMEOUT, e);
    }
    return DEFAULT_TIMEOUT;
  }

  @Override
  protected TimeoutExecutor createTimeoutExecutor(
      ClassLoader classLoader, final long timeout, String className) {
    return new TimeoutExecutor() {
      @Override
      public long getTimeoutInMillis() {
        return timeout;
      }

      @Override
      public Exception generateTimeoutException(ClassLoader classLoader) {
        return new SocketTimeoutException("Read timed out");
      }

      @Override
      public void run(EnhancerModel enhancerModel) throws Exception {
        // 异步场景不需要在当前线程抛异常
      }
    };
  }

  @Override
  protected String getUrl(Object instance, Object[] object) throws Exception {
    Object httpResponseStatus = object[0];
    Object uri = ReflectUtil.getSuperclassFieldValue(httpResponseStatus, "uri", false);
    String url = ReflectUtil.invokeMethod(uri, "toUrl", new Object[0], false);
    return UrlUtils.getUrlExcludeQueryParameters(url);
  }
}
