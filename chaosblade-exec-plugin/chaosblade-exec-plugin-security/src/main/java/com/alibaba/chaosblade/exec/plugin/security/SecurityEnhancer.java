package com.alibaba.chaosblade.exec.plugin.security;

import com.alibaba.chaosblade.exec.common.aop.BeforeEnhancer;
import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherModel;
import com.alibaba.chaosblade.exec.common.util.ReflectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * @author liubin477@163.com
 */
public class SecurityEnhancer extends BeforeEnhancer {

    private static final Logger log = LoggerFactory.getLogger(SecurityEnhancer.class);

    @Override
    public EnhancerModel doBeforeAdvice(ClassLoader classLoader,
                                        String className,
                                        Object object,
                                        Method method,
                                        Object[] methodArguments) throws Exception {
        Object username = methodArguments[0];
        log.info("[SecurityEnhancer] username: {}", username);

        MatcherModel matcherModel = new MatcherModel();
        matcherModel.add("username", username);
        return new EnhancerModel(classLoader, matcherModel);
    }
}
