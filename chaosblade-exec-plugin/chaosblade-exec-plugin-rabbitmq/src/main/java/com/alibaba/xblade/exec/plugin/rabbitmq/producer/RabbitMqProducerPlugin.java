package com.alibaba.xblade.exec.plugin.rabbitmq.producer;

import com.alibaba.xblade.exec.common.aop.Enhancer;
import com.alibaba.xblade.exec.common.aop.PointCut;
import com.alibaba.xblade.exec.plugin.rabbitmq.RabbitMqPlugin;

/** @author raygenyang@163.com */
public class RabbitMqProducerPlugin extends RabbitMqPlugin {
  @Override
  public String getName() {
    return "producer";
  }

  @Override
  public PointCut getPointCut() {
    return new RabbitMqProducerPointCut();
  }

  @Override
  public Enhancer getEnhancer() {
    return new RabbitMqProducerEnhancer();
  }
}
