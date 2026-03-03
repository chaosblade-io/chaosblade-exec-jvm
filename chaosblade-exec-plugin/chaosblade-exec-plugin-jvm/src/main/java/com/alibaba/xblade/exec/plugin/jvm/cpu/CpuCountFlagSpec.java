package com.alibaba.xblade.exec.plugin.jvm.cpu;

import com.alibaba.xblade.exec.common.model.FlagSpec;
import com.alibaba.xblade.exec.plugin.jvm.JvmConstant;

/** @author Changjun Xiao */
public class CpuCountFlagSpec implements FlagSpec {

  @Override
  public String getName() {
    return JvmConstant.FLAG_NAME_CPU_COUNT;
  }

  @Override
  public String getDesc() {
    return "Binding cpu core count";
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
