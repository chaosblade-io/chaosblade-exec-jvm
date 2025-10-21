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

package com.alibaba.chaosblade.exec.plugin.kafka.consumer;

import com.alibaba.chaosblade.exec.common.aop.BeforeEnhancer;
import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherModel;
import com.alibaba.chaosblade.exec.common.util.ReflectUtil;
import com.alibaba.chaosblade.exec.plugin.kafka.KafkaConstant;
import com.alibaba.chaosblade.exec.plugin.kafka.model.ConsumerTopicMatcher;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** @author ljzhxx@gmail.com */
public class KafkaConsumerEnhancer extends BeforeEnhancer implements KafkaConstant {

  private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumerEnhancer.class);

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
    MatcherModel matcherModel = new MatcherModel();
    matcherModel.add(CONSUMER_KEY, "true");
    // groupId
    String groupId = this.findGroupId(object);
    matcherModel.add(GROUP_ID_KEY, groupId);
    // topic
    Set<String> topicKeySet = this.findTopic(object);
    EnhancerModel enhancerModel = new EnhancerModel(classLoader, matcherModel);
    enhancerModel.addCustomMatcher(TOPIC_KEY, topicKeySet, ConsumerTopicMatcher.getInstance());
    LOGGER.debug("kafka consumer, groupId = {}, topic = {}", groupId, topicKeySet);
    return enhancerModel;
  }

  private Set<String> findTopic(Object object) throws Exception {
    Set<String> topicKeySet = new HashSet<>();
    Object delegate = ReflectUtil.getFieldValue(object, "delegate", false);
    Object target = delegate == null ? object : delegate; // 3.6 or 3.7+
    Object metadata = ReflectUtil.getFieldValue(target, "metadata", false);
    Object subscription = ReflectUtil.getFieldValue(metadata, "subscription", false);
    if (subscription != null) {
      Object subscriptionList = ReflectUtil.getFieldValue(subscription, "subscription", false);
      if (subscriptionList != null) {
        topicKeySet.addAll((HashSet<String>) subscriptionList);
      }
    } else {
      Map<String, Object> topics = ReflectUtil.getFieldValue(metadata, "topics", false);
      if (topics != null) {
        topicKeySet.addAll(topics.keySet());
      }
    }
    return topicKeySet;
  }

  private String findGroupId(Object object) throws Exception {
    Object groupId = ReflectUtil.getFieldValue(object, "groupId", false);
    if (groupId instanceof String) {
      return (String) groupId; // 2.5-
    }
    if (groupId instanceof Optional) {
      Optional<String> op = (Optional<String>) groupId;
      return op.orElse(null); // [2.5, 3.7)
    }
    // 3.7+
    Object delegate = ReflectUtil.getFieldValue(object, "delegate", false);
    if (delegate == null) {
      return null;
    }
    Object gid = ReflectUtil.getFieldValue(delegate, "groupId", false);
    if (gid instanceof Optional) {
      return ((Optional<String>) gid).orElse(null); // 3.7+ ClassicKafkaConsumer/LegacyKafkaConsumer
    }
    Object groupMetadata = ReflectUtil.getFieldValue(delegate, "groupMetadata", false);
    if (groupMetadata instanceof Optional) { // [3.7,3.9) AsyncKafkaConsumer
      Optional<?> groupMetadataOptional = (Optional<?>) groupMetadata;
      if (groupMetadataOptional.isPresent()) {
        // ConsumerGroupMetadata
        Object meta = groupMetadataOptional.get();
        return ReflectUtil.getFieldValue(meta, "groupId", false);
      }
    }
    if (groupMetadata instanceof AtomicReference) { // 3.9+ AsyncKafkaConsumer
      AtomicReference<Optional<?>> ref = (AtomicReference<Optional<?>>) groupMetadata;
      Optional<?> o = ref.get();
      if (o != null && o.isPresent()) {
        Object meta = o.get();
        return ReflectUtil.getFieldValue(meta, "groupId", false);
      }
    }
    return null;
  }
}
