package com.alibaba.xblade.exec.plugin.rabbitmq.consumer;

import com.alibaba.xblade.exec.common.aop.Enhancer;
import com.alibaba.xblade.exec.common.aop.PointCut;
import com.alibaba.xblade.exec.plugin.rabbitmq.RabbitMqPlugin;

/** @author raygenyang@163.com */
public class RabbitMqConsumerPlugin extends RabbitMqPlugin {
  @Override
  public String getName() {
    return "consumer";
  }

  @Override
  public PointCut getPointCut() {
    return new RabbitMqConsumerPointCut();
  }

  @Override
  public Enhancer getEnhancer() {
    return new RabbitMqConsumerEnhancer();
  }
}
