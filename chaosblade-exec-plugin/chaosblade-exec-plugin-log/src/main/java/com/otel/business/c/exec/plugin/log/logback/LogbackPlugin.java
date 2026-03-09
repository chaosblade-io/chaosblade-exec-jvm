package com.otel.business.c.exec.plugin.log.logback;

import com.otel.business.c.exec.common.aop.Enhancer;
import com.otel.business.c.exec.common.aop.PointCut;
import com.otel.business.c.exec.plugin.log.LogConstant;
import com.otel.business.c.exec.plugin.log.LogPlugin;

/** @author shizhi.zhu@qunar.com */
public class LogbackPlugin extends LogPlugin {
  @Override
  public String getName() {
    return LogConstant.LOGBACK_KEY;
  }

  @Override
  public PointCut getPointCut() {
    return new LogbackPointCut();
  }

  @Override
  public Enhancer getEnhancer() {
    return new LogbackEnhancer();
  }
}
