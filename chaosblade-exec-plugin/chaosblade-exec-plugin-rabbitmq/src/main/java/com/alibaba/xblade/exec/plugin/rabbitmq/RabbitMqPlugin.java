package com.alibaba.xblade.exec.plugin.rabbitmq;

import com.alibaba.xblade.exec.common.aop.Plugin;
import com.alibaba.xblade.exec.common.model.ModelSpec;
import com.alibaba.xblade.exec.plugin.rabbitmq.model.RabbitMqModelSpec;

/** @author raygenyang@163.com */
public abstract class RabbitMqPlugin implements Plugin, RabbitMqConstant {

  @Override
  public ModelSpec getModelSpec() {
    return new RabbitMqModelSpec();
  }
}
