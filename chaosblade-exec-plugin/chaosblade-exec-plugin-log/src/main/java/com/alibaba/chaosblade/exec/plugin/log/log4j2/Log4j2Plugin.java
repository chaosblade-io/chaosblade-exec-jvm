package com.alibaba.xblade.exec.plugin.log.log4j2;

import com.alibaba.xblade.exec.common.aop.Enhancer;
import com.alibaba.xblade.exec.common.aop.PointCut;
import com.alibaba.xblade.exec.plugin.log.LogConstant;
import com.alibaba.xblade.exec.plugin.log.LogPlugin;

/** @author orion233 */
public class Log4j2Plugin extends LogPlugin {
  @Override
  public String getName() {
    return LogConstant.LOG4J2_KEY;
  }

  @Override
  public PointCut getPointCut() {
    return new Log4j2PointCut();
  }

  @Override
  public Enhancer getEnhancer() {
    return new Log4j2Enhancer();
  }
}
