package com.alibaba.chaosblade.exec.plugin.rabbitmq.consumer;

import com.alibaba.chaosblade.exec.common.aop.PointCut;
import com.alibaba.chaosblade.exec.common.aop.matcher.ClassInfo;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.ClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.MethodMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.NameMethodMatcher;
import com.alibaba.chaosblade.exec.plugin.rabbitmq.RabbitMqConstant;

/** @author raygenyang@163.com */
public class RabbitMqConsumerPointCut implements PointCut, RabbitMqConstant {

  @Override
  public ClassMatcher getClassMatcher() {
    return new ClassMatcher() {
      @Override
      public boolean isMatched(String className, ClassInfo classInfo) {
        for (String ifs : classInfo.getInterfaces()) {
          if (ifs.equals(CONSUMER_CLASS)) {
            return true;
          }
        }
        return false;
      }
    };
  }

  @Override
  public MethodMatcher getMethodMatcher() {
    return new NameMethodMatcher(DELIVERY_METHOD);
  }
}
