package com.alibaba.chaosblade.exec.plugin.security;

import com.alibaba.chaosblade.exec.common.aop.BeforeEnhancer;
import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * @author liubin@njzfit.cn
 */
public class SecurityEnhancer extends BeforeEnhancer {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityEnhancer.class);

    @Override
    public EnhancerModel doBeforeAdvice(ClassLoader classLoader,
                                        String className,
                                        Object object,
                                        Method method,
                                        Object[] methodArguments) throws Exception {
        if (methodArguments.length < 1) {
            LOGGER.warn("argument's length less than 1, className:{}, methodName:{}", className, method.getName());
            return null;
        }
        Object username = methodArguments[0];

        MatcherModel matcherModel = new MatcherModel();
        matcherModel.add(SecurityConstant.PARAM_USERNAME, username);
        return new EnhancerModel(classLoader, matcherModel);
    }
}
