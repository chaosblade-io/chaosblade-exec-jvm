package com.alibaba.chaosblade.exec.plugin.rocketmq;

import com.alibaba.chaosblade.exec.common.model.matcher.BasePredicateMatcherSpec;

/**
 * producer Group
 *
 * @author haibin
 * @date 2019-07-24
 * @email haibin.lhb@alibaba-inc.com
 */
public class ProducerGroupMatcherSpec extends BasePredicateMatcherSpec implements RocketMqConstant {
    @Override
    public String getName() {
        return FLAG_PRODUCER_GROUP;
    }

    @Override
    public String getDesc() {
        return "Message producer group";
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
