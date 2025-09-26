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
