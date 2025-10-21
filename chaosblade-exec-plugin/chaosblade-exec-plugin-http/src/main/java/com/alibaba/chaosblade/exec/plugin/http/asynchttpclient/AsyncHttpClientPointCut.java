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

import com.alibaba.chaosblade.exec.common.aop.PointCut;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.ClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.InterfaceClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.NameClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.OrClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.MethodMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.NameMethodMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.OrMethodMatcher;

/** @author shizhi.zhu@qunar.com */
public class AsyncHttpClientPointCut implements PointCut {
  @Override
  public ClassMatcher getClassMatcher() {
    return new OrClassMatcher()
        .or(new InterfaceClassMatcher("com.ning.http.client.AsyncHandler"))
        .or(new NameClassMatcher("com.ning.http.client.providers.netty.request.NettyRequestSender"))
        .or(new NameClassMatcher("com.ning.http.client.providers.netty.handler.HttpProtocol"));
  }

  @Override
  public MethodMatcher getMethodMatcher() {
    MethodMatcher onStatusReceivedMatcher = new NameMethodMatcher("onStatusReceived");

    MethodMatcher newNettyRequestAndResponseFutureMatcher =
        new NameMethodMatcher("newNettyRequestAndResponseFuture");

    MethodMatcher handleMatcher = new NameMethodMatcher("handle");
    return new OrMethodMatcher()
        .or(onStatusReceivedMatcher)
        .or(newNettyRequestAndResponseFutureMatcher)
        .or(handleMatcher);
  }
}
