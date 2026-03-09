package com.otel.business.c.exec.plugin.rabbitmq.model;

import com.otel.business.c.exec.common.model.matcher.BasePredicateMatcherSpec;
import com.otel.business.c.exec.plugin.rabbitmq.RabbitMqConstant;

/** @author raygenyang@163.com */
public class TopicMatcherSpec extends BasePredicateMatcherSpec implements RabbitMqConstant {
  @Override
  public String getName() {
    return TOPIC_KEY;
  }

  @Override
  public String getDesc() {
    return "";
  }

  @Override
  public boolean noArgs() {
    return false;
  }

  @Override
  public boolean required() {
    return false;
  }
}
