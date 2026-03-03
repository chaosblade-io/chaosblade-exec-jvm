package com.alibaba.xblade.exec.plugin.rocketmq;

import com.alibaba.xblade.exec.common.aop.Enhancer;
import com.alibaba.xblade.exec.common.aop.Plugin;
import com.alibaba.xblade.exec.common.aop.PointCut;
import com.alibaba.xblade.exec.common.model.ModelSpec;

/**
 * @author RinaisSuper
 * @date 2019-07-23
 * @email rinalhb@icloud.com
 */
public class RocketMqPlugin implements Plugin, RocketMqConstant {
  @Override
  public String getName() {
    return PLUGIN_NAME;
  }

  @Override
  public ModelSpec getModelSpec() {
    return new RocketMqModelSpec();
  }

  @Override
  public PointCut getPointCut() {
    return new RocketMqPointCut();
  }

  @Override
  public Enhancer getEnhancer() {
    return new RocketMqEnhancer();
  }
}
