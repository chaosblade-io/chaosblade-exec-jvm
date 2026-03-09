package com.otel.business.c.exec.plugin.security;

import com.otel.business.c.exec.common.aop.PointCut;
import com.otel.business.c.exec.common.aop.matcher.clazz.ClassMatcher;
import com.otel.business.c.exec.common.aop.matcher.clazz.InterfaceClassMatcher;
import com.otel.business.c.exec.common.aop.matcher.method.MethodMatcher;
import com.otel.business.c.exec.common.aop.matcher.method.NameMethodMatcher;

/** @author liubin@njzfit.cn */
public class SecurityPointCut implements PointCut {

  @Override
  public ClassMatcher getClassMatcher() {
    return new InterfaceClassMatcher(SecurityConstant.CLASS_UserDetailsService);
  }

  @Override
  public MethodMatcher getMethodMatcher() {
    return new NameMethodMatcher(SecurityConstant.METHOD_UserDetailsService$loadUserByUsername);
  }
}
