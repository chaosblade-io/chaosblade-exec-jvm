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

package com.alibaba.chaosblade.exec.plugin.rabbitmq;

/** @author raygenyang@163.com */
public interface RabbitMqConstant {

  String TARGET_NAME = "rabbitmq";

  String PRODUCER_KEY = "producer";

  String CONSUMER_KEY = "consumer";

  String EXCHANGE_KEY = "exchange";

  String ROUTING_KEY = "routingkey";

  String TOPIC_KEY = "topic";

  String PUBLISH_METHOD = "basicPublish";

  String CHANNELN_CLASS = "com.rabbitmq.client.impl.ChannelN";

  String CONSUMER_CLASS = "com.rabbitmq.client.Consumer";

  String DELIVERY_METHOD = "handleDelivery";

  String GET_EXCHANGE_METHOD = "getExchange";

  String GET_ROUTING_KEY_METHOD = "getRoutingKey";
}
