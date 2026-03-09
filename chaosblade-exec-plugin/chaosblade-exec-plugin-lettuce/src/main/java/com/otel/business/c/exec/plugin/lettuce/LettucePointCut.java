package com.otel.business.c.exec.plugin.lettuce;

import static com.otel.business.c.exec.plugin.lettuce.LettuceConstants.CLASS;
import static com.otel.business.c.exec.plugin.lettuce.LettuceConstants.METHOD;

import com.otel.business.c.exec.common.aop.PointCut;
import com.otel.business.c.exec.common.aop.matcher.clazz.ClassMatcher;
import com.otel.business.c.exec.common.aop.matcher.clazz.NameClassMatcher;
import com.otel.business.c.exec.common.aop.matcher.method.MethodMatcher;
import com.otel.business.c.exec.common.aop.matcher.method.NameMethodMatcher;

/** @author yefei */
public class LettucePointCut implements PointCut {

  @Override
  public ClassMatcher getClassMatcher() {
    NameClassMatcher nameClassMatcher = new NameClassMatcher(CLASS);
    return nameClassMatcher;
  }

  @Override
  public MethodMatcher getMethodMatcher() {
    NameMethodMatcher nameMethodMatcher = new NameMethodMatcher(METHOD);
    return nameMethodMatcher;
  }
}
