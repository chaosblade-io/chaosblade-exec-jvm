package com.alibaba.chaosblade.exec.plugin.rabbitmq.model;

import com.alibaba.chaosblade.exec.common.aop.PredicateResult;
import com.alibaba.chaosblade.exec.common.exception.ExperimentException;
import com.alibaba.chaosblade.exec.common.model.FrameworkModelSpec;
import com.alibaba.chaosblade.exec.common.model.Model;
import com.alibaba.chaosblade.exec.common.model.handler.PreCreateInjectionModelHandler;
import com.alibaba.chaosblade.exec.common.model.handler.PreDestroyInjectionModelHandler;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherModel;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherSpec;
import com.alibaba.chaosblade.exec.plugin.rabbitmq.RabbitMqConstant;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author raygenyang@163.com
 */
public class RabbitMqModelSpec extends FrameworkModelSpec implements RabbitMqConstant {
    @Override
    protected List<MatcherSpec> createNewMatcherSpecs() {
        ArrayList<MatcherSpec> arrayList = new ArrayList<MatcherSpec>();
        arrayList.add(new ConsumerMatcherSpec());
        arrayList.add(new ExchangeMatcherSpec());
        arrayList.add(new ProducerMatcherSpec());
        arrayList.add(new RoutingKeyMatcherSpec());
        arrayList.add(new TopicMatcherSpec());
        return arrayList;
    }

    @Override
    public String getTarget() {
        return TARGET_NAME;
    }

    @Override
    public String getShortDesc() {
        return "rabbitmq experiment";
    }

    @Override
    public String getLongDesc() {
        return "rabbitmq experiment for testing service delay and exception.";
    }

    @Override
    public String getExample() {
        return "blade c rabbitmq delay --time 3000 --producer";
    }

    @Override
    protected PredicateResult preMatcherPredicate(Model model) {
        if (model == null) {
            return PredicateResult.fail("matcher not found for rabbitmq");
        }
        MatcherModel matcher = model.getMatcher();
        Set<String> keySet = matcher.getMatchers().keySet();
        for (String key : keySet) {
            if (key.equals(CONSUMER_KEY) || key.equals(PRODUCER_KEY)) {
                return PredicateResult.success();
            }
        }
        return PredicateResult.fail("less necessary matcher is consumer or producer for rabbitmq");
    }
}
