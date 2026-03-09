package com.otel.business.c.exec.plugin.rabbitmq.consumer;

import com.otel.business.c.exec.common.aop.Enhancer;
import com.otel.business.c.exec.common.aop.PointCut;
import com.otel.business.c.exec.plugin.rabbitmq.RabbitMqPlugin;

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
