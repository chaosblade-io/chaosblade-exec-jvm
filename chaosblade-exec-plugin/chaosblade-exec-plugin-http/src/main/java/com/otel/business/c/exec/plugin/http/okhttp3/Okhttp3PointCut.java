package com.otel.business.c.exec.plugin.http.okhttp3;

import com.otel.business.c.exec.common.aop.PointCut;
import com.otel.business.c.exec.common.aop.matcher.clazz.ClassMatcher;
import com.otel.business.c.exec.common.aop.matcher.clazz.NameClassMatcher;
import com.otel.business.c.exec.common.aop.matcher.method.MethodMatcher;
import com.otel.business.c.exec.common.aop.matcher.method.NameMethodMatcher;

/**
 * okhttp3 的注入位点
 *
 * @author pengpj
 * @date 2020-7-13
 */
public class Okhttp3PointCut implements PointCut {

  @Override
  public ClassMatcher getClassMatcher() {
    return new NameClassMatcher("okhttp3.RealCall");
  }

  @Override
  public MethodMatcher getMethodMatcher() {
    return new NameMethodMatcher("execute");
  }
}
