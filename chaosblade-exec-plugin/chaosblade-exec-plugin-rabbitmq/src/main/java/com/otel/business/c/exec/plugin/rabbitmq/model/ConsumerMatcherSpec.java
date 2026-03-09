package com.otel.business.c.exec.plugin.rabbitmq.model;

import com.otel.business.c.exec.common.model.matcher.BasePredicateMatcherSpec;
import com.otel.business.c.exec.plugin.rabbitmq.RabbitMqConstant;

/** @author raygenyang@163.com */
public class ConsumerMatcherSpec extends BasePredicateMatcherSpec implements RabbitMqConstant {
  @Override
  public String getName() {
    return CONSUMER_KEY;
  }

  @Override
  public String getDesc() {
    return "";
  }

  @Override
  public boolean noArgs() {
    return true;
  }

  @Override
  public boolean required() {
    return false;
  }
}
