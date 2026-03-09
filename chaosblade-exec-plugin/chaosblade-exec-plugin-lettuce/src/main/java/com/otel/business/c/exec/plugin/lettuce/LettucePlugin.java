package com.otel.business.c.exec.plugin.lettuce;

import com.otel.business.c.exec.common.aop.Enhancer;
import com.otel.business.c.exec.common.aop.Plugin;
import com.otel.business.c.exec.common.aop.PointCut;
import com.otel.business.c.exec.common.model.ModelSpec;

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
