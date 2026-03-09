package com.otel.business.c.exec.plugin.tars.client;

import com.otel.business.c.exec.common.aop.PointCut;
import com.otel.business.c.exec.common.aop.matcher.clazz.ClassMatcher;
import com.otel.business.c.exec.common.aop.matcher.clazz.NameClassMatcher;
import com.otel.business.c.exec.common.aop.matcher.method.*;

/**
 * @author saikei
 * @email lishiji@huya.com
 */
public class TarsClientPointCut implements PointCut {

  @Override
  public ClassMatcher getClassMatcher() {
    return new NameClassMatcher("com.qq.tars.client.rpc.tars.TarsInvoker");
  }

  @Override
  public MethodMatcher getMethodMatcher() {
    return new ParameterMethodMatcher(
        new String[] {"com.qq.tars.client.rpc.ServantInvokeContext"},
        1,
        ParameterMethodMatcher.EQUAL);
  }
}
