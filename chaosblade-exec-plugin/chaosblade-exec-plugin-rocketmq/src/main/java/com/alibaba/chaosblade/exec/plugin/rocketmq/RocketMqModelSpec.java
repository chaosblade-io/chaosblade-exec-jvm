package com.alibaba.chaosblade.exec.plugin.rocketmq;

import com.alibaba.chaosblade.exec.common.model.FrameworkModelSpec;
import com.alibaba.chaosblade.exec.common.model.action.ActionSpec;
import com.alibaba.chaosblade.exec.common.model.action.delay.DelayActionSpec;
import com.alibaba.chaosblade.exec.common.model.action.exception.ThrowCustomExceptionActionSpec;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherSpec;

import java.util.ArrayList;
import java.util.List;

/**
 * @author RinaisSuper
 * @date 2019-07-24
 * @email rinalhb@icloud.com
 */
public class RocketMqModelSpec extends FrameworkModelSpec implements RocketMqConstant {

    public RocketMqModelSpec() {
        addActionExample();
    }

    private void addActionExample() {
        List<ActionSpec> actions = getActions();
        for (ActionSpec action : actions) {
            if (action instanceof DelayActionSpec) {
                action.setLongDesc("RocketMq delay experiment");
                action.setExample("# Do a delay 3s experiment on the RocketMq when topic=xx consumerGroup=xx\n" +
                        "blade create rocketmq delay --time=3000 --topic=xx --consumerGroup=xx");
            } else if (action instanceof ThrowCustomExceptionActionSpec) {
                action.setLongDesc("RocketMq throws custom exception experiment");
                action.setExample("# Do a throw custom exception experiment on the RocketMq when topic=xx consumerGroup=xx\n" +
                               "blade create rocketmq throwCustomException --exception java.lang.Exception --topic=xx --consumerGroup=xx");
            }
        }
    }

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

}
