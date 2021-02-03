package com.alibaba.chaosblade.exec.plugin.kafka.model;

import com.alibaba.chaosblade.exec.common.model.matcher.BasePredicateMatcherSpec;
import com.alibaba.chaosblade.exec.plugin.kafka.KafkaConstant;

/**
 * @author ljzhxx@gmail.com
 */
public class TopicMatcherSpec extends BasePredicateMatcherSpec implements KafkaConstant {
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
