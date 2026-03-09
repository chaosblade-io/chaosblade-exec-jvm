package com.otel.business.c.exec.plugin.feign;

import com.otel.business.c.exec.common.aop.PointCut;
import com.otel.business.c.exec.common.aop.matcher.MethodInfo;
import com.otel.business.c.exec.common.aop.matcher.clazz.ClassMatcher;
import com.otel.business.c.exec.common.aop.matcher.clazz.NameClassMatcher;
import com.otel.business.c.exec.common.aop.matcher.method.MethodMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** @author guoyu486@gmail.com */
public class FeignProducerPointCut implements PointCut, FeignConstant {

  private static final Logger LOGGER = LoggerFactory.getLogger(FeignProducerPointCut.class);

  @Override
  public ClassMatcher getClassMatcher() {
    return new NameClassMatcher(ENHANCER_CLASS);
  }

  @Override
  public MethodMatcher getMethodMatcher() {
    return new MethodMatcher() {
      @Override
      public boolean isMatched(String methodName, MethodInfo methodInfo) {
        return methodName.equals(METHOD);
      }
    };
  }
}
