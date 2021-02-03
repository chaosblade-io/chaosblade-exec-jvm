package com.alibaba.chaosblade.exec.plugin.kafka.producer;

import com.alibaba.chaosblade.exec.common.aop.PointCut;
import com.alibaba.chaosblade.exec.common.aop.matcher.MethodInfo;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.ClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.NameClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.MethodMatcher;
import com.alibaba.chaosblade.exec.plugin.kafka.KafkaConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ljzhxx@gmail.com
 */
public class KafkaProducerPointCut implements PointCut, KafkaConstant {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducerPointCut.class);

    @Override
    public ClassMatcher getClassMatcher() {
        return new NameClassMatcher(PRODUCER_CLASS);
    }

    @Override
    public MethodMatcher getMethodMatcher() {
        return new MethodMatcher() {
            @Override
            public boolean isMatched(String methodName, MethodInfo methodInfo) {
                return methodName.equals(SEND) && methodInfo.getParameterTypes().length == 1;
            }
        };
    }
}
