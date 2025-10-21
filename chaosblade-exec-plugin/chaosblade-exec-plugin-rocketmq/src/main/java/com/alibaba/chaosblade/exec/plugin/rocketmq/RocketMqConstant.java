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

/**
 * @author RinaisSuper
 * @date 2019-07-23
 * @email rinalhb@icloud.com
 */
public interface RocketMqConstant {

  public static String PLUGIN_NAME = "rocketmq";

  public static String REMOTEING_SUPER_CLASS_ALIBABA =
      "com.alibaba.rocketmq.remoting.netty.NettyRemotingAbstract";

  public static String REMOTEING_SUPER_CLASS_APACHE =
      "org.apache.rocketmq.remoting.netty.NettyRemotingAbstract";

  public static String SYNC_INVOKE_METHOD = "invokeSyncImpl";

  public static String ASYNC_INVOKE_METHOD = "invokeAsyncImpl";

  public static String ONEWAY_INVOKE_METHOD = "invokeOnewayImpl";

  public static String FLAG_NAME_TOPIC = "topic";

  public static String FLAG_PRODUCER_GROUP = "producerGroup";

  public static String FLAG_CONSUMER_GROUP = "consumerGroup";
}
