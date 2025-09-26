package com.alibaba.chaosblade.exec.plugin.feign;

import com.alibaba.chaosblade.exec.common.aop.Enhancer;
import com.alibaba.chaosblade.exec.common.aop.Plugin;
import com.alibaba.chaosblade.exec.common.aop.PointCut;
import com.alibaba.chaosblade.exec.common.model.ModelSpec;
import com.alibaba.chaosblade.exec.plugin.feign.model.FeignModelSpec;

/** @author guoyu486@gmail.com */
public class FeignPlugin implements Plugin, FeignConstant {

  @Override
  public ModelSpec getModelSpec() {
    return new FeignModelSpec();
  }

  @Override
  public String getName() {
    return "feign";
  }

  @Override
  public PointCut getPointCut() {
    return new FeignProducerPointCut();
  }

  @Override
  public Enhancer getEnhancer() {
    return new FeignProducerEnhancer();
  }
}
