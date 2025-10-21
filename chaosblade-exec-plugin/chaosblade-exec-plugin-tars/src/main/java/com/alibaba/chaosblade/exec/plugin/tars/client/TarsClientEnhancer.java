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

package com.alibaba.chaosblade.exec.plugin.tars.client;

import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.common.model.action.delay.BaseTimeoutExecutor;
import com.alibaba.chaosblade.exec.common.model.action.delay.TimeoutExecutor;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherModel;
import com.alibaba.chaosblade.exec.common.util.JsonUtil;
import com.alibaba.chaosblade.exec.common.util.ReflectUtil;
import com.alibaba.chaosblade.exec.plugin.tars.TarsConstant;
import com.alibaba.chaosblade.exec.plugin.tars.TarsEnhancer;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author saikei
 * @email lishiji@huya.com
 */
public class TarsClientEnhancer extends TarsEnhancer {

  private static final String SERVANT_PROXY_CONFIG = "config";
  private static final String GET_SYNC_TIMEOUT = "getSyncTimeout";
  private static final String GET_ASYNC_TIMEOUT = "getAsyncTimeout";
  private static final String GET_INVOKER = "getInvoker";
  private static final String OBJ_NAME = "objName";
  private static final String GET_METHOD_NAME = "getMethodName";
  private static final String TARS_TIMEOUT_EXCEPTION = "com.qq.tars.rpc.exc.TimeoutException";

  private static final Logger LOGGER = LoggerFactory.getLogger(TarsClientEnhancer.class);

  private static boolean isAsync(String methodName) {
    return methodName != null && methodName.startsWith("async_");
  }

  @Override
  public EnhancerModel doBeforeAdvice(
      ClassLoader classLoader,
      String className,
      Object object,
      Method method,
      Object[] methodArguments)
      throws Exception {
    Object servantInvokerContext = methodArguments[0];
    if (object == null || servantInvokerContext == null) {
      LOGGER.warn("The necessary parameter is null");
      return null;
    }
    Object invoker =
        ReflectUtil.invokeMethod(servantInvokerContext, GET_INVOKER, new Object[0], false);
    String servantName = ReflectUtil.getSuperclassFieldValue(invoker, OBJ_NAME, false);
    String functionName =
        ReflectUtil.invokeMethod(servantInvokerContext, GET_METHOD_NAME, new Object[0], false);

    Object config = ReflectUtil.getSuperclassFieldValue(invoker, SERVANT_PROXY_CONFIG, false);
    int timeout = getTimeOut(functionName, config);

    MatcherModel matcherModel = new MatcherModel();
    matcherModel.add(TarsConstant.SERVANT_NAME, servantName);
    matcherModel.add(TarsConstant.FUNCTION_NAME, functionName);
    matcherModel.add(TarsConstant.CLIENT, "true");

    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("tars matchers: {}", JsonUtil.writer().writeValueAsString(matcherModel));
    }

    EnhancerModel enhancerModel = new EnhancerModel(classLoader, matcherModel);
    enhancerModel.setTimeoutExecutor(createTimeoutExecutor(classLoader, timeout, className));

    return enhancerModel;
  }

  @Override
  protected TimeoutExecutor createTimeoutExecutor(
      ClassLoader classLoader, long timeout, final String className) {
    return new BaseTimeoutExecutor(classLoader, timeout) {
      @Override
      public Exception generateTimeoutException(ClassLoader classLoader) {
        Class timeoutExceptionClass;
        try {
          timeoutExceptionClass = classLoader.loadClass(TARS_TIMEOUT_EXCEPTION);
          Class[] paramTypes = {String.class};
          Object[] params = {"chaosblade-mock-TimeoutException,timeout=" + timeoutInMillis};
          Constructor con = timeoutExceptionClass.getConstructor(paramTypes);
          return (Exception) con.newInstance(params);
        } catch (ClassNotFoundException e) {

          LOGGER.error("chaosblade-tars", "Can not find " + TARS_TIMEOUT_EXCEPTION, e);
        } catch (Exception e) {
          LOGGER.error("chaosblade-tars", "Can not generate " + TARS_TIMEOUT_EXCEPTION, e);
        }
        return new RuntimeException(TarsConstant.TIMEOUT_EXCEPTION_MSG);
      }
    };
  }

  private int getTimeOut(String methodName, Object servantProxyConfig) throws Exception {
    boolean isAsync = isAsync(methodName);
    if (isAsync) {
      return ReflectUtil.invokeMethod(servantProxyConfig, GET_ASYNC_TIMEOUT, new Object[0], false);
    }
    return ReflectUtil.invokeMethod(servantProxyConfig, GET_SYNC_TIMEOUT, new Object[0], false);
  }
}
