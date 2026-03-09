package com.otel.business.c.exec.plugin.log;

import com.otel.business.c.exec.common.aop.Plugin;
import com.otel.business.c.exec.common.model.ModelSpec;
import com.otel.business.c.exec.plugin.log.model.LogModelSpec;

/** @author shizhi.zhu@qunar.com */
public abstract class LogPlugin implements Plugin {
  @Override
  public ModelSpec getModelSpec() {
    return new LogModelSpec();
  }
}
