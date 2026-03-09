package com.otel.business.c.exec.plugin.feign.model;

import com.otel.business.c.exec.common.model.matcher.BasePredicateMatcherSpec;
import com.otel.business.c.exec.plugin.feign.FeignConstant;

/** @author guoyu486@gmail.com */
public class UrlMatcherSpec extends BasePredicateMatcherSpec implements FeignConstant {
  @Override
  public String getName() {
    return TEMPLATE_URL;
  }

  @Override
  public String getDesc() {
    return "the feign api url";
  }

  @Override
  public boolean noArgs() {
    return false;
  }

  @Override
  public boolean required() {
    return true;
  }
}
