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

package com.alibaba.chaosblade.exec.plugin.rabbitmq.consumer;

import com.alibaba.chaosblade.exec.common.aop.BeforeEnhancer;
import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherModel;
import com.alibaba.chaosblade.exec.common.util.ReflectUtil;
import com.alibaba.chaosblade.exec.plugin.rabbitmq.RabbitMqConstant;
import java.lang.reflect.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** @author raygenyang@163.com */
public class RabbitMqConsumerEnhancer extends BeforeEnhancer implements RabbitMqConstant {

  private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMqConsumerEnhancer.class);

  @Override
  public EnhancerModel doBeforeAdvice(
      ClassLoader classLoader,
      String className,
      Object object,
      Method method,
      Object[] methodArguments)
      throws Exception {
    Object envelope = methodArguments[1];
    String exchange =
        ReflectUtil.invokeMethod(envelope, GET_EXCHANGE_METHOD, new Object[] {}, false);
    String routingKey =
        ReflectUtil.invokeMethod(envelope, GET_ROUTING_KEY_METHOD, new Object[] {}, false);
    MatcherModel matcherModel = new MatcherModel();
    matcherModel.add(EXCHANGE_KEY, exchange);
    matcherModel.add(ROUTING_KEY, routingKey);
    matcherModel.add(TOPIC_KEY, routingKey);
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug(
          "exchange: {}, routingKey: {}, matcherModel: {}", exchange, routingKey, matcherModel);
    }
    EnhancerModel enhancerModel = new EnhancerModel(classLoader, matcherModel);
    enhancerModel.addMatcher(CONSUMER_KEY, "true");
    return enhancerModel;
  }
}
