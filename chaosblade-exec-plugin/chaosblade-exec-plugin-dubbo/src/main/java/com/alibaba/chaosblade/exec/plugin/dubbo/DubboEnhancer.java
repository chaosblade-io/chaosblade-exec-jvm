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

package com.alibaba.chaosblade.exec.plugin.dubbo;

import com.alibaba.chaosblade.exec.common.aop.BeforeEnhancer;
import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.common.aop.matcher.busi.BusinessParamMatcher;
import com.alibaba.chaosblade.exec.common.constant.ModelConstant;
import com.alibaba.chaosblade.exec.common.model.action.delay.TimeoutExecutor;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherModel;
import com.alibaba.chaosblade.exec.common.util.*;
import com.alibaba.chaosblade.exec.plugin.dubbo.model.DubboThreadPoolFullExecutor;
import java.lang.reflect.Method;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** @author Changjun Xiao */
public abstract class DubboEnhancer extends BeforeEnhancer {

  public static final String DEFAULT_VERSION = "0.0.0";
  public static final String GET_URL = "getUrl";
  public static final String GET_PARAMETER = "getParameter";
  public static final String APPLICATION_KEY = "application";
  public static final String GET_SERVICE_KEY = "getServiceKey";
  public static final String GET_METHOD_NAME = "getMethodName";
  public static final String GET_ARGUMENTS = "getArguments";
  public static final String GET_ATTACHMENTS = "getAttachments";
  public static final String GENERIC = "generic";
  public static final String SPLIT_TOKEN = ":";
  public static final String GROUP_SEP = "/";
  public static final String GET_INVOKER = "getInvoker";
  public static final String RECEIVED_METHOD = "received";

  public static final int INVALID_POS = -1;
  private static final Logger LOGGER = LoggerFactory.getLogger(DubboEnhancer.class);

  @Override
  public EnhancerModel doBeforeAdvice(
      ClassLoader classLoader,
      String className,
      Object object,
      Method method,
      Object[] methodArguments)
      throws Exception {
    if (method.getName().equals(RECEIVED_METHOD)) {
      // received method for thread pool experiment
      DubboThreadPoolFullExecutor.INSTANCE.setWrappedChannelHandler(object);
      return null;
    }

    Object invocation = methodArguments[0];
    if (object == null || invocation == null) {
      LOGGER.warn("The necessary parameter is null.");
      return null;
    }
    Object url = getUrl(object, invocation);
    if (url == null) {
      LOGGER.warn("Url is null, can not get necessary values.");
      return null;
    }
    String methodName = null;
    String provideGeneric =
        ReflectUtil.invokeMethod(url, GET_PARAMETER, new Object[] {GENERIC}, false);
    boolean consumerGeneric = isConsumerGeneric(object, invocation);
    if (Boolean.valueOf(provideGeneric) || consumerGeneric) {
      Object[] arguments =
          ReflectUtil.invokeMethod(invocation, GET_ARGUMENTS, new Object[0], false);
      if (arguments.length > 1 && arguments[0] instanceof String) {
        methodName = (String) arguments[0];
      }
      LOGGER.debug("generic is true, methodName:{}", methodName);
    } else {
      methodName = ReflectUtil.invokeMethod(invocation, GET_METHOD_NAME, new Object[0], false);
      LOGGER.debug("generic is false, methodName:{}", methodName);
    }
    if (methodName == null) {
      LOGGER.warn("methodName is null, can not get necessary values.");
      return null;
    }
    String[] serviceAndVersionGroup = getServiceNameWithVersionGroup(invocation, url);

    MatcherModel matcherModel = new MatcherModel();
    String appName =
        ReflectUtil.invokeMethod(url, GET_PARAMETER, new Object[] {APPLICATION_KEY}, false);
    matcherModel.add(DubboConstant.APP_KEY, appName);
    matcherModel.add(DubboConstant.SERVICE_KEY, serviceAndVersionGroup[0]);
    matcherModel.add(DubboConstant.VERSION_KEY, serviceAndVersionGroup[1]);
    if (2 < serviceAndVersionGroup.length && null != serviceAndVersionGroup[2]) {
      matcherModel.add(DubboConstant.GROUP_KEY, serviceAndVersionGroup[2]);
    }
    matcherModel.add(DubboConstant.METHOD_KEY, methodName);
    int timeout = getTimeout(methodName, object, invocation);
    matcherModel.add(DubboConstant.TIMEOUT_KEY, timeout + "");
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("dubbo matchers: {}", JsonUtil.writer().writeValueAsString(matcherModel));
    }
    EnhancerModel enhancerModel = new EnhancerModel(classLoader, matcherModel);
    enhancerModel.setTimeoutExecutor(createTimeoutExecutor(classLoader, timeout, className));
    try {
      Map<String, Map<String, String>> businessParams = getBusinessParams(invocation);
      if (businessParams != null) {
        enhancerModel.addCustomMatcher(
            ModelConstant.BUSINESS_PARAMS, businessParams, BusinessParamMatcher.getInstance());
      }
    } catch (Exception e) {
      LOGGER.warn("Getting business params occurs exception,return null", e);
    }
    postDoBeforeAdvice(enhancerModel);
    return enhancerModel;
  }

  protected boolean isConsumerGeneric(Object object, Object invocation) throws Exception {
    String methodName = ReflectUtil.invokeMethod(invocation, GET_METHOD_NAME, new Object[0], false);
    Class type = ReflectUtil.getSuperclassFieldValue(object, "type", false);
    String clazz = type.getName();
    return methodName.contains("$invoke")
        && (clazz.equalsIgnoreCase("org.apache.dubbo.rpc.service.GenericService")
            || clazz.equalsIgnoreCase("com.alibaba.dubbo.rpc.service.GenericService"));
  }

  protected abstract void postDoBeforeAdvice(EnhancerModel enhancerModel);

  /**
   * Get service name with version
   *
   * @param invocation
   * @param url
   * @return
   * @throws Exception
   */
  public String[] getServiceNameWithVersionGroup(Object invocation, Object url) throws Exception {
    // com.alibaba.dubbo.demo.DemoService | com.alibaba.dubbo.demo.DemoService:1.0
    String serviceKey = ReflectUtil.invokeMethod(url, GET_SERVICE_KEY, new Object[0], false);
    if (serviceKey == null) {
      LOGGER.warn("Service key is null in dubbo consumer. invocation: {}", invocation);
      return null;
    } else {
      int groupSep = serviceKey.indexOf(GROUP_SEP);
      String group = null;
      if (INVALID_POS != groupSep) {
        group = serviceKey.substring(0, groupSep);
        serviceKey = serviceKey.substring(groupSep + 1);
      }

      String[] serviceAndVersion = serviceKey.split(SPLIT_TOKEN);
      if (serviceAndVersion.length == 1) {
        return new String[] {serviceAndVersion[0], DEFAULT_VERSION, group};
      }
      return new String[] {serviceAndVersion[0], serviceAndVersion[1], group};
    }
  }

  /**
   * Get service timeout
   *
   * @param method
   * @param instance
   * @param invocation
   * @return
   */
  protected abstract int getTimeout(String method, Object instance, Object invocation);

  /**
   * Get service url
   *
   * @param instance
   * @param invocation
   * @return
   * @throws Exception
   */
  protected abstract Object getUrl(Object instance, Object invocation) throws Exception;

  /**
   * Create timeout executor
   *
   * @param classLoader
   * @param timeout
   * @param className
   * @return
   */
  protected abstract TimeoutExecutor createTimeoutExecutor(
      ClassLoader classLoader, long timeout, String className);

  /**
   * The dubbo version is than 2.7.0
   *
   * @param className
   * @return
   */
  protected boolean isThan2700Version(String className) {
    return className.startsWith("org.apache");
  }

  /**
   * Get b-params
   *
   * @param invocation
   * @return
   * @throws Exception
   */
  protected abstract Map<String, Map<String, String>> getBusinessParams(Object invocation)
      throws Exception;
}
