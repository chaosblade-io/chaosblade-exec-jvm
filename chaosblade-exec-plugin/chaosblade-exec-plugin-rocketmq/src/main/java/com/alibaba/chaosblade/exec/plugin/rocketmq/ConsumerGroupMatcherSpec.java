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

import com.alibaba.chaosblade.exec.common.model.matcher.BasePredicateMatcherSpec;

/**
 * consumerGroup
 *
 * @author RinaisSuper
 * @date 2019-07-24
 * @email rinalhb@icloud.com
 */
public class ConsumerGroupMatcherSpec extends BasePredicateMatcherSpec implements RocketMqConstant {
  @Override
  public String getName() {
    return FLAG_CONSUMER_GROUP;
  }

  @Override
  public String getDesc() {
    return "Message consumer group";
  }

  @Override
  public boolean noArgs() {
    return false;
  }

  @Override
  public boolean required() {
    return false;
  }
}
