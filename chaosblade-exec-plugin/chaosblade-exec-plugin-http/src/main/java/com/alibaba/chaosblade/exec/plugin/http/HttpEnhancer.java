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

package com.alibaba.chaosblade.exec.plugin.http;

import com.alibaba.chaosblade.exec.common.aop.BeforeEnhancer;
import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.common.aop.matcher.busi.BusinessParamMatcher;
import com.alibaba.chaosblade.exec.common.constant.ModelConstant;
import com.alibaba.chaosblade.exec.common.model.action.delay.BaseTimeoutExecutor;
import com.alibaba.chaosblade.exec.common.model.action.delay.TimeoutExecutor;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherModel;
import com.alibaba.chaosblade.exec.common.util.FlagUtil;
import com.alibaba.chaosblade.exec.common.util.JsonUtil;
import com.alibaba.chaosblade.exec.plugin.http.model.CallPointMatcher;
import java.lang.reflect.Method;
import java.net.SocketTimeoutException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author yuhan
 *
 * @package: com.alibaba.chaosblade.exec.plugin.http @Date 2019-05-22 20:35
 */
public abstract class HttpEnhancer extends BeforeEnhancer {

  private static final Logger LOGGER = LoggerFactory.getLogger(HttpEnhancer.class);

  @Override
  public EnhancerModel doBeforeAdvice(
      ClassLoader classLoader,
      String className,
      Object object,
      Method method,
      Object[] methodArguments)
      throws Exception {
    MatcherModel matcherModel = new MatcherModel();
    matcherModel.add(HttpConstant.URI_KEY, getUrl(object, methodArguments));
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("http matchers: {}", JsonUtil.writer().writeValueAsString(matcherModel));
    }
    EnhancerModel enhancerModel = new EnhancerModel(classLoader, matcherModel);
    int timeout = getTimeout(object, methodArguments);
    enhancerModel.setMethod(method).setObject(object).setMethodArguments(methodArguments);
    postDoBeforeAdvice(enhancerModel);
    if (shouldAddCallPoint()) {
      StackTraceElement[] stackTrace = new NullPointerException().getStackTrace();
      enhancerModel.addCustomMatcher(
          HttpConstant.CALL_POINT_KEY, stackTrace, CallPointMatcher.getInstance());
    }
    enhancerModel.setTimeoutExecutor(createTimeoutExecutor(classLoader, timeout, className));
    try {
      Map<String, Map<String, String>> businessParams =
          getBusinessParams(className, object, method, methodArguments);
      if (businessParams != null) {
        enhancerModel.addCustomMatcher(
            ModelConstant.BUSINESS_PARAMS, businessParams, BusinessParamMatcher.getInstance());
      }
    } catch (Exception e) {
      LOGGER.warn("Getting business params  occurs exception", e);
    }
    return enhancerModel;
  }

  /**
   * Get b-params
   *
   * @param instance
   * @param methodArguments
   * @return
   * @throws Exception
   */
  protected abstract Map<String, Map<String, String>> getBusinessParams(
      String className, Object instance, Method method, Object[] methodArguments) throws Exception;

  protected boolean shouldAddCallPoint() {
    return FlagUtil.hasFlag("http", HttpConstant.CALL_POINT_KEY);
  }

  /**
   * Get service timeout
   *
   * @param instance
   * @return
   */
  protected abstract int getTimeout(Object instance, Object[] methodArguments);

  /**
   * Create timeout executor
   *
   * @param classLoader
   * @param timeout
   * @param className
   * @return
   */
  protected TimeoutExecutor createTimeoutExecutor(
      ClassLoader classLoader, long timeout, String className) {
    return new BaseTimeoutExecutor(classLoader, timeout) {
      @Override
      public Exception generateTimeoutException(ClassLoader classLoader) {
        return new SocketTimeoutException("Read timed out");
      }
    };
  }

  protected abstract void postDoBeforeAdvice(EnhancerModel enhancerModel);

  /**
   * 获取Http Url
   *
   * @param instance
   * @param object
   * @return
   */
  protected abstract String getUrl(Object instance, Object[] object) throws Exception;
}
