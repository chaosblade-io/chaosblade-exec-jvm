package com.otel.business.c.exec.plugin.jvm.script.model;

import com.otel.business.c.exec.common.model.FlagSpec;
import com.otel.business.c.exec.plugin.jvm.JvmConstant;

/**
 * @author yefei
 * @create 2020-10-12 10:01
 */
public class ScriptExternalJarFlagSpec implements FlagSpec {

  @Override
  public String getName() {
    return JvmConstant.FLAG_NAME_EXTERNAL_JAR;
  }

  @Override
  public String getDesc() {
    return "add external jar file";
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
