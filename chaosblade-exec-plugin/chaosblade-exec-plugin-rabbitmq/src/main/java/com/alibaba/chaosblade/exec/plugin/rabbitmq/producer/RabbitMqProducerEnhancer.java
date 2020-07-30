package com.alibaba.chaosblade.exec.plugin.rabbitmq.producer;

import com.alibaba.chaosblade.exec.common.aop.BeforeEnhancer;
import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherModel;
import com.alibaba.chaosblade.exec.plugin.rabbitmq.RabbitMqConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * @author raygenyang@163.com
 */
public class RabbitMqProducerEnhancer extends BeforeEnhancer implements RabbitMqConstant {

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMqProducerEnhancer.class);

    @Override
    public EnhancerModel doBeforeAdvice(ClassLoader classLoader, String className, Object object, Method method, Object[] methodArguments) throws Exception {
        String exchange = (String) methodArguments[0];
        String routingKey = (String) methodArguments[1];
        MatcherModel matcherModel = new MatcherModel();
        matcherModel.add(EXCHANGE_KEY, exchange);
        matcherModel.add(ROUTING_KEY, routingKey);
        matcherModel.add(TOPIC_KEY, routingKey);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("exchange: {}, routingKey: {}, matcherModel: {}", exchange, routingKey, matcherModel);
        }
        EnhancerModel enhancerModel = new EnhancerModel(classLoader, matcherModel);
        enhancerModel.addMatcher(PRODUCER_KEY, "true");
        return enhancerModel;
    }
}
