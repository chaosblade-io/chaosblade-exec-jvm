package com.alibaba.xblade.exec.plugin.lettuce;

import static com.alibaba.xblade.exec.plugin.lettuce.LettuceConstants.VALUE;

import com.alibaba.xblade.exec.common.model.FlagSpec;

/** @author yefei */
public class LettuceValueFlagSpec implements FlagSpec {

  @Override
  public String getName() {
    return VALUE;
  }

  @Override
  public String getDesc() {
    return "value set";
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
