package com.alibaba.xblade.exec.plugin.log;

import com.alibaba.xblade.exec.common.aop.Plugin;
import com.alibaba.xblade.exec.common.model.ModelSpec;
import com.alibaba.xblade.exec.plugin.log.model.LogModelSpec;

/** @author shizhi.zhu@qunar.com */
public abstract class LogPlugin implements Plugin {
  @Override
  public ModelSpec getModelSpec() {
    return new LogModelSpec();
  }
}
