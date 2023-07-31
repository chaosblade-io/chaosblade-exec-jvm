package com.alibaba.chaosblade.exec.plugin.kafka.model;

import com.alibaba.chaosblade.exec.common.model.matcher.BasePredicateMatcherSpec;
import com.alibaba.chaosblade.exec.plugin.kafka.KafkaConstant;

/**
 * @author oncefalse@gmail.com
 */
public class GroupIdMatcherSpec extends BasePredicateMatcherSpec implements KafkaConstant {
    @Override
    public String getName() {
        return GROUP_ID_KEY;
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
