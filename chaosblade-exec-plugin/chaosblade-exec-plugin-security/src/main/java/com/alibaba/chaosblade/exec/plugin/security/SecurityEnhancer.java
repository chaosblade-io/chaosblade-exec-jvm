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
        if (methodArguments == null || methodArguments.length == 0) {
            log.warn("The necessary parameters is null or length is not equal 1, {}",
                    methodArguments != null ? methodArguments.length : null);
            return null;
        }

        Object username = null;

        if (method.getName().equals(SecurityConstant.METHOD_UserDetailsService$loadUserByUsername)) {
            username = methodArguments[0];
        }

        if (username == null) {
            return null;
        }

        MatcherModel matcherModel = new MatcherModel();
        matcherModel.add("username", username);
        log.info("[SecurityEnhancer] username: {}", username);
        return new EnhancerModel(classLoader, matcherModel);
    }
}
