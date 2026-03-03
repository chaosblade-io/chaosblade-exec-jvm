package com.alibaba.xblade.exec.plugin.lettuce;

import com.alibaba.xblade.exec.common.aop.Enhancer;
import com.alibaba.xblade.exec.common.aop.Plugin;
import com.alibaba.xblade.exec.common.aop.PointCut;
import com.alibaba.xblade.exec.common.model.ModelSpec;

/** @author yefei */
public class LettucePlugin implements Plugin {
  @Override
  public String getName() {
    return "lettuce plugin";
  }

  @Override
  public ModelSpec getModelSpec() {
    return new LettuceModeSpec();
  }

  @Override
  public PointCut getPointCut() {
    return new LettucePointCut();
  }

  @Override
  public Enhancer getEnhancer() {
    return new LettuceEnhancer();
  }
}
