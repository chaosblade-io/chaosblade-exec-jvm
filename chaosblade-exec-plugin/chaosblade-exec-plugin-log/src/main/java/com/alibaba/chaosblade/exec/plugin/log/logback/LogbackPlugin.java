package com.alibaba.xblade.exec.plugin.log.logback;

import com.alibaba.xblade.exec.common.aop.Enhancer;
import com.alibaba.xblade.exec.common.aop.PointCut;
import com.alibaba.xblade.exec.plugin.log.LogConstant;
import com.alibaba.xblade.exec.plugin.log.LogPlugin;

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
