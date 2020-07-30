package com.alibaba.chaosblade.exec.plugin.rabbitmq.producer;

import com.alibaba.chaosblade.exec.common.aop.PointCut;
import com.alibaba.chaosblade.exec.common.aop.matcher.MethodInfo;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.ClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.NameClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.MethodMatcher;
import com.alibaba.chaosblade.exec.plugin.rabbitmq.RabbitMqConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author raygenyang@163.com
 */
public class RabbitMqProducerPointCut implements PointCut, RabbitMqConstant {

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMqProducerPointCut.class);

    @Override
    public ClassMatcher getClassMatcher() {
        return new NameClassMatcher(CHANNELN_CLASS);
    }

    @Override
    public MethodMatcher getMethodMatcher() {
        return new MethodMatcher() {

            @Override
            public boolean isMatched(String methodName, MethodInfo methodInfo) {
                return methodName.equals(PUBLISH_METHOD) && methodInfo.getParameterTypes().length == 6;
            }
        };
    }
}
