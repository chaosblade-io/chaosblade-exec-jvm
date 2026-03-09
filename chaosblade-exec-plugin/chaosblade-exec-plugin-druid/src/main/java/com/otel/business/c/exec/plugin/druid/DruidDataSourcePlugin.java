package com.otel.business.c.exec.plugin.druid;

import com.otel.business.c.exec.common.aop.Enhancer;
import com.otel.business.c.exec.common.aop.Plugin;
import com.otel.business.c.exec.common.aop.PointCut;
import com.otel.business.c.exec.common.model.ModelSpec;

/** @author Changjun Xiao */
public class DruidDataSourcePlugin implements Plugin {

  @Override
  public String getName() {
    return DruidConstant.DRUID_DS_PLUGIN_NAME;
  }

  @Override
  public ModelSpec getModelSpec() {
    return new DruidModelSpec();
  }

  @Override
  public PointCut getPointCut() {
    return new DruidDataSourcePointCut();
  }

  @Override
  public Enhancer getEnhancer() {
    return new DruidDataSourceEnhancer();
  }
}
