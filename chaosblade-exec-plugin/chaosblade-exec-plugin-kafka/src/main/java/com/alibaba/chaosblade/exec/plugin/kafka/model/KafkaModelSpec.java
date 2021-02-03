package com.alibaba.chaosblade.exec.plugin.kafka.model;

import com.alibaba.chaosblade.exec.common.aop.PredicateResult;
import com.alibaba.chaosblade.exec.common.model.FrameworkModelSpec;
import com.alibaba.chaosblade.exec.common.model.Model;
import com.alibaba.chaosblade.exec.common.model.action.ActionSpec;
import com.alibaba.chaosblade.exec.common.model.action.delay.DelayActionSpec;
import com.alibaba.chaosblade.exec.common.model.action.exception.ThrowCustomExceptionActionSpec;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherModel;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherSpec;
import com.alibaba.chaosblade.exec.plugin.kafka.KafkaConstant;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author ljzhxx@gmail.com
 */
public class KafkaModelSpec extends FrameworkModelSpec implements KafkaConstant {

    public KafkaModelSpec() {
        addActionExample();
    }

    private void addActionExample() {
        List<ActionSpec> actions = getActions();
        for (ActionSpec action : actions) {
            if (action instanceof DelayActionSpec) {
                action.setLongDesc("Kafka delay experiment");
                action.setExample("# Delay when the producer sends the message\n" +
                        "blade create kafka delay --time 3000 --producer");
            } else if (action instanceof ThrowCustomExceptionActionSpec) {
                action.setLongDesc("Kafka throws custom exception experiment");
                action.setExample("# Throw exception when the producer sends the message\n" +
                        "blade create kafka throwCustomException --exception java.lang.Exception --exception-message mock-beans-exception --producer");
            }
        }
    }

    @Override
    protected List<MatcherSpec> createNewMatcherSpecs() {
        ArrayList<MatcherSpec> arrayList = new ArrayList<MatcherSpec>();
        arrayList.add(new ConsumerMatcherSpec());
        arrayList.add(new ProducerMatcherSpec());
        arrayList.add(new TopicMatcherSpec());
        return arrayList;
    }

    @Override
    public String getTarget() {
        return TARGET_NAME;
    }

    @Override
    public String getShortDesc() {
        return "kafka experiment";
    }

    @Override
    public String getLongDesc() {
        return "kafka experiment for testing service delay and exception.";
    }

    @Override
    protected PredicateResult preMatcherPredicate(Model model) {
        if (model == null) {
            return PredicateResult.fail("matcher not found for kafka");
        }
        MatcherModel matcher = model.getMatcher();
        Set<String> keySet = matcher.getMatchers().keySet();
        for (String key : keySet) {
            if (key.equals(CONSUMER_KEY) || key.equals(PRODUCER_KEY)) {
                return PredicateResult.success();
            }
        }
        return PredicateResult.fail("less necessary matcher is consumer or producer for kafka");
    }
}
