package com.otel.business.c.exec.plugin.security;

import com.otel.business.c.exec.common.aop.Enhancer;
import com.otel.business.c.exec.common.aop.Plugin;
import com.otel.business.c.exec.common.aop.PointCut;
import com.otel.business.c.exec.common.model.ModelSpec;

/** @author liubin@njzfit.cn */
public class SecurityPlugin implements Plugin {

  @Override
  public String getName() {
    return SecurityConstant.PLUGIN_NAME;
  }

  @Override
  public ModelSpec getModelSpec() {
    return new SecurityModelSpec();
  }

  @Override
  public PointCut getPointCut() {
    return new SecurityPointCut();
  }

  @Override
  public Enhancer getEnhancer() {
    return new SecurityEnhancer();
  }
}
