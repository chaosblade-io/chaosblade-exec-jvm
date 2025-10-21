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

package com.alibaba.chaosblade.exec.plugin.kafka.producer;

import com.alibaba.chaosblade.exec.common.aop.BeforeEnhancer;
import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherModel;
import com.alibaba.chaosblade.exec.common.util.ReflectUtil;
import com.alibaba.chaosblade.exec.plugin.kafka.KafkaConstant;
import java.lang.reflect.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** @author ljzhxx@gmail.com */
public class KafkaProducerEnhancer extends BeforeEnhancer implements KafkaConstant {

  private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducerEnhancer.class);

  @Override
  public EnhancerModel doBeforeAdvice(
      ClassLoader classLoader,
      String className,
      Object object,
      Method method,
      Object[] methodArguments)
      throws Exception {

    if (methodArguments == null || object == null) {
      LOGGER.warn("The necessary parameter is null.");
      return null;
    }

    Object topicKeyObject = methodArguments[0];
    String topicKey = ReflectUtil.getFieldValue(topicKeyObject, "topic", false);
    MatcherModel matcherModel = new MatcherModel();
    matcherModel.add(TOPIC_KEY, topicKey);
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("producer topicKey: {}, matcherModel: {}", topicKey, matcherModel);
    }
    EnhancerModel enhancerModel = new EnhancerModel(classLoader, matcherModel);
    enhancerModel.addMatcher(PRODUCER_KEY, "true");
    return enhancerModel;
  }
}
