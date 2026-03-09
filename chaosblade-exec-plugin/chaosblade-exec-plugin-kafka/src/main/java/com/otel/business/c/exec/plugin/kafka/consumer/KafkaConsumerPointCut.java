package com.otel.business.c.exec.plugin.kafka.consumer;

import com.otel.business.c.exec.common.aop.PointCut;
import com.otel.business.c.exec.common.aop.matcher.MethodInfo;
import com.otel.business.c.exec.common.aop.matcher.clazz.ClassMatcher;
import com.otel.business.c.exec.common.aop.matcher.clazz.NameClassMatcher;
import com.otel.business.c.exec.common.aop.matcher.method.MethodMatcher;
import com.otel.business.c.exec.plugin.kafka.KafkaConstant;

/** @author ljzhxx@gmail.com */
public class KafkaConsumerPointCut implements PointCut, KafkaConstant {

  @Override
  public ClassMatcher getClassMatcher() {
    return new NameClassMatcher(CONSUMER_CLASS);
  }

  @Override
  public MethodMatcher getMethodMatcher() {
    return new MethodMatcher() {
      @Override
      public boolean isMatched(String methodName, MethodInfo methodInfo) {
        return methodName.equals(POLL) && methodInfo.getParameterTypes().length == 1;
      }
    };
  }
}
