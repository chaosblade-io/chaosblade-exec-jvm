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

import com.alibaba.chaosblade.exec.common.aop.BeforeEnhancer;
import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherModel;
import com.alibaba.chaosblade.exec.common.util.JsonUtil;
import com.alibaba.chaosblade.exec.common.util.ReflectUtil;
import java.lang.reflect.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author RinaisSuper
 * @date 2019-07-24
 * @email rinalhb@icloud.com
 */
public class RocketMqEnhancer extends BeforeEnhancer implements RocketMqConstant {

  private static final Logger LOGGER = LoggerFactory.getLogger(RocketMqEnhancer.class);
  public static String CLASS_PULL_MESSAGE_REQUEST_HEADER_ALIBABA =
      "com.alibaba.rocketmq.common.protocol.header.PullMessageRequestHeader";
  public static String CLASS_PULL_MESSAGE_REQUEST_HEADER_APACHE =
      "org.apache.rocketmq.common.protocol.header.PullMessageRequestHeader";

  public static String CLASS_SEND_MESSAGE_REQUEST_HEADER_ALIBABA =
      "com.alibaba.rocketmq.common.protocol.header.SendMessageRequestHeader";
  public static String CLASS_SEND_MESSAGE_REQUEST_HEADER_APACHE =
      "org.apache.rocketmq.common.protocol.header.SendMessageRequestHeader";

  public static String CLASS_SEND_MESSAGE_REQUEST_HEADERV2_ALIBABA =
      "com.alibaba.rocketmq.common.protocol.header.SendMessageRequestHeaderV2";
  public static String CLASS_SEND_MESSAGE_REQUEST_HEADERV2_APACHE =
      "org.apache.rocketmq.common.protocol.header.SendMessageRequestHeaderV2";

  public static String CLASS_REMOTEING_COMMAND_CLASS_ALIBABA =
      "com.alibaba.rocketmq.remoting.protocol.RemotingCommand";
  public static String CLASS_REMOTEING_COMMAND_CLASS_APACHE =
      "org.apache.rocketmq.remoting.protocol.RemotingCommand";

  private static String FIELD_CUSTOM_HEADER = "customHeader";

  @Override
  public EnhancerModel doBeforeAdvice(
      ClassLoader classLoader,
      String className,
      Object object,
      Method method,
      Object[] methodArguments)
      throws Exception {
    MatcherModel matcherModel = new MatcherModel();
    String remotingCommandClassName = CLASS_REMOTEING_COMMAND_CLASS_ALIBABA;
    if (isApache(className)) {
      remotingCommandClassName = CLASS_REMOTEING_COMMAND_CLASS_APACHE;
    }
    Object remoteingCommnadRequest =
        selectParamByClassName(classLoader, methodArguments, remotingCommandClassName);

    if (remoteingCommnadRequest == null) {
      return new EnhancerModel(classLoader, matcherModel);
    } else {
      Object header =
          ReflectUtil.getFieldValue(remoteingCommnadRequest, FIELD_CUSTOM_HEADER, false);
      if (header == null) {
        return new EnhancerModel(classLoader, matcherModel);
      }
      String topic = null;
      String consumerGroup = null;
      String producerGroup = null;
      if (isPullMessageHeader(classLoader, header, className)) {
        topic = ReflectUtil.getFieldValue(header, FLAG_NAME_TOPIC, false);
        consumerGroup = ReflectUtil.getFieldValue(header, FLAG_CONSUMER_GROUP, false);
      } else if (isSendMessageHeader(classLoader, header, className)) {
        topic = ReflectUtil.getFieldValue(header, FLAG_NAME_TOPIC, false);
        producerGroup = ReflectUtil.getFieldValue(header, FLAG_PRODUCER_GROUP, false);
      } else if (isSendMessageHeaderV2(classLoader, header, className)) {
        topic = ReflectUtil.getFieldValue(header, "b", false);
        producerGroup = ReflectUtil.getFieldValue(header, "a", false);
      }
      matcherModel.add(FLAG_NAME_TOPIC, topic);
      matcherModel.add(FLAG_CONSUMER_GROUP, consumerGroup);
      matcherModel.add(FLAG_PRODUCER_GROUP, producerGroup);
      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug("rocketmq matchers: {}", JsonUtil.writer().writeValueAsString(matcherModel));
      }
      return new EnhancerModel(classLoader, matcherModel);
    }
  }

  private boolean isSendMessageHeader(ClassLoader classLoader, Object header, String className) {
    if (isApache(className)) {
      return ReflectUtil.isAssignableFrom(
          classLoader, header.getClass(), CLASS_SEND_MESSAGE_REQUEST_HEADER_APACHE);
    }
    return ReflectUtil.isAssignableFrom(
        classLoader, header.getClass(), CLASS_SEND_MESSAGE_REQUEST_HEADER_ALIBABA);
  }

  private boolean isSendMessageHeaderV2(ClassLoader classLoader, Object header, String className) {
    if (isApache(className)) {
      return ReflectUtil.isAssignableFrom(
          classLoader, header.getClass(), CLASS_SEND_MESSAGE_REQUEST_HEADERV2_APACHE);
    }
    return ReflectUtil.isAssignableFrom(
        classLoader, header.getClass(), CLASS_SEND_MESSAGE_REQUEST_HEADERV2_ALIBABA);
  }

  private boolean isPullMessageHeader(ClassLoader classLoader, Object header, String className) {
    if (isApache(className)) {
      return ReflectUtil.isAssignableFrom(
          classLoader, header.getClass(), CLASS_PULL_MESSAGE_REQUEST_HEADER_APACHE);
    }
    return ReflectUtil.isAssignableFrom(
        classLoader, header.getClass(), CLASS_PULL_MESSAGE_REQUEST_HEADER_ALIBABA);
  }

  private Object selectParamByClassName(
      ClassLoader classLoader, Object[] methodArguments, String filterClassName) {
    for (Object object : methodArguments) {
      if (ReflectUtil.isAssignableFrom(classLoader, object.getClass(), filterClassName)) {
        return object;
      }
    }
    return null;
  }

  protected boolean isApache(String className) {
    return className.startsWith("org.apache");
  }
}
