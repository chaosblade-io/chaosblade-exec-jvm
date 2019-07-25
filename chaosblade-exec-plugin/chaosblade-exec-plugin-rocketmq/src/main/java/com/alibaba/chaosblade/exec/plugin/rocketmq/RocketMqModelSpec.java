package com.alibaba.chaosblade.exec.plugin.rocketmq;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.chaosblade.exec.common.model.FrameworkModelSpec;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherSpec;

/**
 * @author RinaisSuper
 * @date 2019-07-24
 * @email haibin.lhb@alibaba-inc.com
 */
public class RocketMqModelSpec extends FrameworkModelSpec implements RocketMqConstant {

    @Override
    protected List<MatcherSpec> createNewMatcherSpecs() {
        ArrayList<MatcherSpec> arrayList = new ArrayList<MatcherSpec>();
        arrayList.add(new ConsumerGroupMatcherSpec());
        arrayList.add(new ProducerGroupMatcherSpec());
        arrayList.add(new TopicMatcherSpec());
        return arrayList;
    }

    @Override
    public String getTarget() {
        return PLUGIN_NAME;
    }

    @Override
    public String getShortDesc() {
        return "Rocketmq experiment,can make message send or pull delay and exception";
    }

    @Override
    public String getLongDesc() {
        return "Rocketmq experiment,can make message send or pull delay and exception,default if you not set "
            + "[producerGroup,consumerGroup],will effect both send and pull message,if you only set producerGroup for"
            + " specific group,"
            + "will only effect on sendMessage,if you only set consumerGroup,will only effect pullMessage for "
            + "specific group";
    }

    @Override
    public String getExample() {
        return null;
    }
}
