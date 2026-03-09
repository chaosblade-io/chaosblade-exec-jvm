package com.otel.business.c.exec.plugin.http.model;

import com.otel.business.c.exec.common.model.matcher.BasePredicateMatcherSpec;

public class UriMatcherDefSpec extends BasePredicateMatcherSpec {

  @Override
  public String getName() {
    return "uri";
  }

  @Override
  public String getDesc() {
    return "url";
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
