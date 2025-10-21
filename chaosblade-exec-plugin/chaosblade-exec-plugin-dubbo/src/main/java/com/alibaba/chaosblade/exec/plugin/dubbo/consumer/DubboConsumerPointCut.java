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

package com.alibaba.chaosblade.exec.plugin.dubbo.consumer;

import com.alibaba.chaosblade.exec.common.aop.PointCut;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.ClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.NameClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.OrClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.AndMethodMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.MethodMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.NameMethodMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.OrMethodMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.ParameterMethodMatcher;

/** @author Changjun Xiao */
public class DubboConsumerPointCut implements PointCut {

  @Override
  public ClassMatcher getClassMatcher() {
    OrClassMatcher classMatcher = new OrClassMatcher();
    classMatcher
        .or(new NameClassMatcher("com.alibaba.dubbo.rpc.protocol.dubbo.DubboInvoker"))
        .or(new NameClassMatcher("com.alibaba.dubbo.rpc.protocol.thrift.ThriftInvoker"))
        .or(new NameClassMatcher("com.alibaba.dubbo.rpc.protocol.dubbo.ChannelWrappedInvoker"))
        .or(new NameClassMatcher("org.apache.dubbo.rpc.protocol.dubbo.DubboInvoker"))
        .or(new NameClassMatcher("org.apache.dubbo.rpc.protocol.thrift.ThriftInvoker"))
        .or(new NameClassMatcher("org.apache.dubbo.rpc.protocol.dubbo.ChannelWrappedInvoker"));
    return classMatcher;
  }

  @Override
  public MethodMatcher getMethodMatcher() {
    AndMethodMatcher methodMatcher = new AndMethodMatcher();
    ParameterMethodMatcher parameterMethodMatcher =
        new ParameterMethodMatcher(
            new String[] {"com.alibaba.dubbo.rpc.Invocation"}, 1, ParameterMethodMatcher.EQUAL);
    methodMatcher.and(new NameMethodMatcher("doInvoke")).and(parameterMethodMatcher);

    AndMethodMatcher methodMatcherThan2700 = new AndMethodMatcher();
    ParameterMethodMatcher parameterMethodMatcherThan2700 =
        new ParameterMethodMatcher(
            new String[] {"org.apache.dubbo.rpc.Invocation"}, 1, ParameterMethodMatcher.EQUAL);
    methodMatcherThan2700
        .and(new NameMethodMatcher("doInvoke"))
        .and(parameterMethodMatcherThan2700);

    return new OrMethodMatcher().or(methodMatcher).or(methodMatcherThan2700);
  }
}
