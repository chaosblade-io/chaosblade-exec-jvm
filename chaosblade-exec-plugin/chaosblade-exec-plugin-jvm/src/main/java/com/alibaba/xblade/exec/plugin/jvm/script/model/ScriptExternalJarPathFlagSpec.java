package com.alibaba.xblade.exec.plugin.jvm.script.model;

import com.alibaba.xblade.exec.common.model.FlagSpec;
import com.alibaba.xblade.exec.plugin.jvm.JvmConstant;

/**
 * @author yefei
 * @create 2020-10-12 10:01
 */
public class ScriptExternalJarPathFlagSpec implements FlagSpec {

  @Override
  public String getName() {
    return JvmConstant.FLAG_NAME_EXTERNAL_JAR_PATH;
  }

  @Override
  public String getDesc() {
    return "add external jar file from path";
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
