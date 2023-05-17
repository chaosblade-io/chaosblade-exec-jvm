package com.alibaba.chaosblade.exec.plugin.log.log4j2;

import com.alibaba.chaosblade.exec.common.aop.BeforeEnhancer;
import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherModel;
import com.alibaba.chaosblade.exec.plugin.log.LogConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * @author orion233
 */
public class Log4j2Enhancer extends BeforeEnhancer {
    private static final Logger LOGGER = LoggerFactory.getLogger(Log4j2Enhancer.class);

    @Override
    public EnhancerModel doBeforeAdvice(ClassLoader classLoader, String className, Object object, Method method, Object[] methodArguments) throws Exception {
        LOGGER.info("log4j2 do before, classLoader:{}, className:{}, object:{}, method:{}, args:{}", classLoader, className, object, method.getName(), methodArguments);

        EnhancerModel enhancerModel = new EnhancerModel(classLoader, new MatcherModel());
        enhancerModel.addMatcher(LogConstant.LOG4J2_KEY, "true");
        return enhancerModel;
    }
}
