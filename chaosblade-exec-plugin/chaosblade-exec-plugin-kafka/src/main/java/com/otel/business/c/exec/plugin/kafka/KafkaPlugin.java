package com.otel.business.c.exec.plugin.kafka;

import com.otel.business.c.exec.common.aop.Plugin;
import com.otel.business.c.exec.common.model.ModelSpec;
import com.otel.business.c.exec.plugin.kafka.model.KafkaModelSpec;

/** @author ljzhxx@gmail.com */
public abstract class KafkaPlugin implements Plugin, KafkaConstant {

  @Override
  public ModelSpec getModelSpec() {
    return new KafkaModelSpec();
  }
}
