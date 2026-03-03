package com.alibaba.xblade.exec.plugin.gateway;

import com.alibaba.xblade.exec.common.aop.Enhancer;
import com.alibaba.xblade.exec.common.aop.Plugin;
import com.alibaba.xblade.exec.common.aop.PointCut;
import com.alibaba.xblade.exec.common.model.ModelSpec;

/**
 * @Author wb-shd671576
 *
 * @package: com.alibaba.xblade.exec.plugin.gateway @Date 2021-07-29
 */
public class GatewayPlugin implements Plugin {

  @Override
  public String getName() {
    return GatewayConstant.TARGET_NAME;
  }

  @Override
  public ModelSpec getModelSpec() {
    return new GatewayModelSpec();
  }

  @Override
  public PointCut getPointCut() {
    return new GatewayPointCut();
  }

  @Override
  public Enhancer getEnhancer() {
    return new GatewayEnhancer();
  }
}
