package com.alibaba.xblade.exec.plugin.kafka;

import com.alibaba.xblade.exec.common.aop.Plugin;
import com.alibaba.xblade.exec.common.model.ModelSpec;
import com.alibaba.xblade.exec.plugin.kafka.model.KafkaModelSpec;

/** @author ljzhxx@gmail.com */
public abstract class KafkaPlugin implements Plugin, KafkaConstant {

  @Override
  public ModelSpec getModelSpec() {
    return new KafkaModelSpec();
  }
}
