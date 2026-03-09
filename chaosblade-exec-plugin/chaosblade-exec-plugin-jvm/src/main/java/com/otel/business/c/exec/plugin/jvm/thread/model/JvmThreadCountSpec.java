package com.otel.business.c.exec.plugin.jvm.thread.model;

import com.otel.business.c.exec.common.model.FlagSpec;
import com.otel.business.c.exec.plugin.jvm.JvmConstant;

/** @author Changjun Xiao */
public class JvmThreadCountSpec implements FlagSpec {

  @Override
  public String getName() {
    return JvmConstant.ACTION_THREAD_COUNT;
  }

  @Override
  public String getDesc() {
    return "thread count";
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
