package com.alibaba.xblade.exec.plugin.lettuce;

import static com.alibaba.xblade.exec.plugin.lettuce.LettuceConstants.CMD;

import com.alibaba.xblade.exec.common.model.matcher.BasePredicateMatcherSpec;

/**
 * @author yefei
 * @create 2020-11-23 14:53
 */
public class LettuceCmdMatcherSpec extends BasePredicateMatcherSpec {

  @Override
  public String getName() {
    return CMD;
  }

  @Override
  public String getDesc() {
    return "cmd matcher";
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
