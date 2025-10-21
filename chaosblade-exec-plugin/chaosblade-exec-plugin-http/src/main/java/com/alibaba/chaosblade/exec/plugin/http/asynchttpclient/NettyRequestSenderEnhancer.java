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

import com.alibaba.chaosblade.exec.common.aop.BeforeEnhancer;
import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.common.context.GlobalContext;
import com.alibaba.chaosblade.exec.common.util.FlagUtil;
import com.alibaba.chaosblade.exec.common.util.ReflectUtil;
import com.alibaba.chaosblade.exec.plugin.http.HttpConstant;
import com.alibaba.chaosblade.exec.plugin.http.enhancer.InternalPointCut;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** @author shizhi.zhu@qunar.com */
@InternalPointCut(
    className = "com.ning.http.client.providers.netty.request.NettyRequestSender",
    methodName = "newNettyRequestAndResponseFuture")
public class NettyRequestSenderEnhancer extends BeforeEnhancer {
  private static final Logger LOGGER = LoggerFactory.getLogger(NettyRequestSenderEnhancer.class);
  private static final AtomicLong ID_GENERATOR = new AtomicLong();

  @Override
  public EnhancerModel doBeforeAdvice(
      ClassLoader classLoader,
      String className,
      Object object,
      Method method,
      Object[] methodArguments)
      throws Exception {
    if (!shouldAddCallPoint()) {
      return null;
    }
    if (methodArguments.length < 2) {
      LOGGER.warn(
          "argument's length less than 2, can't find Request, className:{}, methodName:{}",
          className,
          method.getName());
      return null;
    }

    Object request = methodArguments[0];
    if (!request
        .getClass()
        .getName()
        .equals("com.ning.http.client.RequestBuilderBase$RequestImpl")) {
      LOGGER.warn(
          "argument is not RequestImpl, className:{}, methodName:{}", className, method.getName());
      return null;
    }
    Object headers = ReflectUtil.invokeMethod(request, "getHeaders");
    String id = String.valueOf(ID_GENERATOR.incrementAndGet());
    ReflectUtil.invokeMethod(headers, "add", new String[] {HttpConstant.REQUEST_ID, id});
    StackTraceElement[] stackTrace = new NullPointerException().getStackTrace();
    GlobalContext.getDefaultInstance().put(id, stackTrace);
    return null;
  }

  protected boolean shouldAddCallPoint() {
    return FlagUtil.hasFlag("http", HttpConstant.CALL_POINT_KEY);
  }
}
