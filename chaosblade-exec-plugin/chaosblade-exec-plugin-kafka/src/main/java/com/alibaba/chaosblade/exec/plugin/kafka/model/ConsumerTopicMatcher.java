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

package com.alibaba.chaosblade.exec.plugin.kafka.model;

import com.alibaba.chaosblade.exec.common.aop.CustomMatcher;
import java.util.Set;

public class ConsumerTopicMatcher implements CustomMatcher {
  private static final ConsumerTopicMatcher INSTANCE = new ConsumerTopicMatcher();

  private ConsumerTopicMatcher() {}

  public static ConsumerTopicMatcher getInstance() {
    return INSTANCE;
  }

  @Override
  public boolean match(String commandValue, Object originValue) {
    Set<String> collection = (Set<String>) originValue;
    return collection.contains(commandValue);
  }

  @Override
  public boolean regexMatch(String commandValue, Object originValue) {
    return this.match(commandValue, originValue);
  }
}
