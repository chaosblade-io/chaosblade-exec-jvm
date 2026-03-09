package com.otel.business.c.exec.plugin.kafka.model;

import com.otel.business.c.exec.common.model.matcher.BasePredicateMatcherSpec;
import com.otel.business.c.exec.plugin.kafka.KafkaConstant;

/** @author ljzhxx@gmail.com */
public class ConsumerMatcherSpec extends BasePredicateMatcherSpec implements KafkaConstant {
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
