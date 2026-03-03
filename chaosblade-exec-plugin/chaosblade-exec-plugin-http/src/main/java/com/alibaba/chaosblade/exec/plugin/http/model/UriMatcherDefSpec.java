package com.alibaba.xblade.exec.plugin.http.model;

import com.alibaba.xblade.exec.common.model.matcher.BasePredicateMatcherSpec;

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
