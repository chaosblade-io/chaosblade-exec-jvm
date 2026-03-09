package com.otel.business.c.exec.plugin.tars.model;

import com.otel.business.c.exec.common.model.matcher.BasePredicateMatcherSpec;
import com.otel.business.c.exec.plugin.tars.TarsConstant;

/**
 * @author saikei
 * @email lishiji@huya.com
 */
public class FunctionNameMatcherSpec extends BasePredicateMatcherSpec {

  @Override
  public String getName() {
    return TarsConstant.FUNCTION_NAME;
  }

  @Override
  public String getDesc() {
    return "The name of function to be invoked";
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
