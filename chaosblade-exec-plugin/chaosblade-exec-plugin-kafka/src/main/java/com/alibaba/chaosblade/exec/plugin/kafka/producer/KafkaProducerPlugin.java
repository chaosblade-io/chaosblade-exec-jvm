package com.alibaba.xblade.exec.plugin.kafka.producer;

import com.alibaba.xblade.exec.common.aop.Enhancer;
import com.alibaba.xblade.exec.common.aop.PointCut;
import com.alibaba.xblade.exec.plugin.kafka.KafkaPlugin;

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
