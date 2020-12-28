package com.alibaba.chaosblade.exec.plugin.kafka.consumer;

import com.alibaba.chaosblade.exec.common.aop.BeforeEnhancer;
import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherModel;
import com.alibaba.chaosblade.exec.plugin.kafka.KafkaConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author ljzhxx@gmail.com
 */
public class KafkaConsumerEnhancer extends BeforeEnhancer implements KafkaConstant {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumerEnhancer.class);

    @Override
    public EnhancerModel doBeforeAdvice(ClassLoader classLoader, String className, Object object, Method method, Object[] methodArguments) throws Exception {

        if (methodArguments == null || object == null) {
            LOGGER.warn("The necessary parameter is null.");
            return null;
        }

        MatcherModel matcherModel = new MatcherModel();
        if (java.util.List.class.isAssignableFrom(methodArguments[0].getClass())) {
            List<String> topicKeySet = (List<String>) methodArguments[0];
            for (String item : topicKeySet) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("consumer topicKey: {}, matcherModel: {}", item, matcherModel);
                }
                matcherModel.add(TOPIC_KEY, item);
            }
        }
        EnhancerModel enhancerModel = new EnhancerModel(classLoader, matcherModel);
        enhancerModel.addMatcher(CONSUMER_KEY, "true");
        return enhancerModel;
    }
}
