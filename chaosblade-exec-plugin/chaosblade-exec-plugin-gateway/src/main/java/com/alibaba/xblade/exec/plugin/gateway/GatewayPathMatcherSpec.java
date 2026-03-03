package com.alibaba.xblade.exec.plugin.gateway;

import com.alibaba.xblade.exec.common.model.matcher.BasePredicateMatcherSpec;

/**
 * @Author wb-shd671576
 *
 * @package: com.alibaba.xblade.exec.plugin.gateway @Date 2021-07-29
 */
public class GatewayPathMatcherSpec extends BasePredicateMatcherSpec {

  @Override
  public String getName() {
    return GatewayConstant.GET_REQUST_PATH;
  }

  @Override
  public String getDesc() {
    return "The gateway path which used";
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
