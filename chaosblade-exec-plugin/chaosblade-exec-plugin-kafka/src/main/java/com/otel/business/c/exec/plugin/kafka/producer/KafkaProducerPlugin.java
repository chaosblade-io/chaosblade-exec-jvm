package com.otel.business.c.exec.plugin.kafka.producer;

import com.otel.business.c.exec.common.aop.Enhancer;
import com.otel.business.c.exec.common.aop.PointCut;
import com.otel.business.c.exec.plugin.kafka.KafkaPlugin;

/** @author ljzhxx@gmail.com */
public class KafkaProducerPlugin extends KafkaPlugin {
  @Override
  public String getName() {
    return "producer";
  }

  @Override
  public PointCut getPointCut() {
    return new KafkaProducerPointCut();
  }

  @Override
  public Enhancer getEnhancer() {
    return new KafkaProducerEnhancer();
  }
}
