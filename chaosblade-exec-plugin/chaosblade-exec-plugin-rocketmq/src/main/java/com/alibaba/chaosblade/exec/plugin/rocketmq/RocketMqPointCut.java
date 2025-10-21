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

package com.alibaba.chaosblade.exec.plugin.rocketmq;

import com.alibaba.chaosblade.exec.common.aop.PointCut;
import com.alibaba.chaosblade.exec.common.aop.matcher.MethodInfo;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.ClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.NameClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.OrClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.MethodMatcher;

/**
 * the rocketmq invoke order
 * producer----->DefaultMQProducerImpl.send---->MQClientAPIImpl.sendMessage ---- invokeAsyncImpl
 * ----->NettyRemotingAbstract----> invokeOnewayImpl invokeSyncImpl
 * conusmer----->DefaultMQPullConsumerImpl.pull---->MQClientAPIImpl.pullMessage---
 *
 * @author RinaisSuper
 * @date 2019-07-24
 * @email rinalhb@icloud.com
 */
public class RocketMqPointCut implements PointCut, RocketMqConstant {

  @Override
  public ClassMatcher getClassMatcher() {
    return new OrClassMatcher()
        .or(new NameClassMatcher(REMOTEING_SUPER_CLASS_ALIBABA))
        .or(new NameClassMatcher(REMOTEING_SUPER_CLASS_APACHE));
  }

  @Override
  public MethodMatcher getMethodMatcher() {
    return new MethodMatcher() {
      @Override
      public boolean isMatched(String methodName, MethodInfo methodInfo) {
        return methodName.equals(SYNC_INVOKE_METHOD)
            || methodName.equals(ASYNC_INVOKE_METHOD)
            || methodName.equals(ONEWAY_INVOKE_METHOD);
      }
    };
  }
}
