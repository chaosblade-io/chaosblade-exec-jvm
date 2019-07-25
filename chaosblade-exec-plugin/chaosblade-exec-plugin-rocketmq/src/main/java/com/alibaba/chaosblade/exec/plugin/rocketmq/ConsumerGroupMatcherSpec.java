package com.alibaba.chaosblade.exec.plugin.rocketmq;

import com.alibaba.chaosblade.exec.common.model.matcher.BasePredicateMatcherSpec;

/**
 * consumerGroup
 *
 * @author RinaisSuper
 * @date 2019-07-24
 * @email rinalhb@icloud.com
 */
public class ConsumerGroupMatcherSpec extends BasePredicateMatcherSpec implements RocketMqConstant {
    @Override
    public String getName() {
        return FLAG_CONSUMER_GROUP;
    }

    @Override
    public String getDesc() {
        return "Message consumer group";
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
