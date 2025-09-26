package com.alibaba.chaosblade.exec.plugin.feign.model;

import com.alibaba.chaosblade.exec.common.model.matcher.BasePredicateMatcherSpec;
import com.alibaba.chaosblade.exec.plugin.feign.FeignConstant;

/** @author guoyu486@gmail.com */
public class ServiceNameMatcherSpec extends BasePredicateMatcherSpec implements FeignConstant {
  @Override
  public String getName() {
    return SERVICE_NAME;
  }

  @Override
  public String getDesc() {
    return "the feign api service name";
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
