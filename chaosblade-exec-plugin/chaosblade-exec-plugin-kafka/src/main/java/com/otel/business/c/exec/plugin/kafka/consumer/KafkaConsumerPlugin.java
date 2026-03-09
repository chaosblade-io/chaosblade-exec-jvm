package com.otel.business.c.exec.plugin.kafka.consumer;

import com.otel.business.c.exec.common.aop.Enhancer;
import com.otel.business.c.exec.common.aop.PointCut;
import com.otel.business.c.exec.plugin.kafka.KafkaPlugin;

/** @author ljzhxx@gmail.com */
public class KafkaConsumerPlugin extends KafkaPlugin {
  @Override
  public String getName() {
    return "consumer";
  }

  @Override
  public PointCut getPointCut() {
    return new KafkaConsumerPointCut();
  }

  @Override
  public Enhancer getEnhancer() {
    return new KafkaConsumerEnhancer();
  }
}
