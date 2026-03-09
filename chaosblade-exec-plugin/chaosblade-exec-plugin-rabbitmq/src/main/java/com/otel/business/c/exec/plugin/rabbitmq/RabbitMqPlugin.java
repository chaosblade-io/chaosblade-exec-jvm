package com.otel.business.c.exec.plugin.rabbitmq;

import com.otel.business.c.exec.common.aop.Plugin;
import com.otel.business.c.exec.common.model.ModelSpec;
import com.otel.business.c.exec.plugin.rabbitmq.model.RabbitMqModelSpec;

/** @author raygenyang@163.com */
public abstract class RabbitMqPlugin implements Plugin, RabbitMqConstant {

  @Override
  public ModelSpec getModelSpec() {
    return new RabbitMqModelSpec();
  }
}
