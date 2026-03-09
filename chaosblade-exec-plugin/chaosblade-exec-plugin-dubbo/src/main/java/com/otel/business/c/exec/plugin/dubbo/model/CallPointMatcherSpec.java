package com.otel.business.c.exec.plugin.dubbo.model;

import com.otel.business.c.exec.common.model.matcher.BasePredicateMatcherSpec;
import com.otel.business.c.exec.plugin.dubbo.DubboConstant;

/** @author shizhi.zhu@qunar.com */
public class CallPointMatcherSpec extends BasePredicateMatcherSpec {
  @Override
  public String getName() {
    return DubboConstant.CALL_POINT_KEY;
  }

  @Override
  public String getDesc() {
    return "the class and method name who send the request";
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
