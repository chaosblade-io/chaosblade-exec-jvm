package com.otel.business.c.exec.plugin.jvm.cpu;

import com.otel.business.c.exec.common.model.FlagSpec;
import com.otel.business.c.exec.plugin.jvm.JvmConstant;

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
