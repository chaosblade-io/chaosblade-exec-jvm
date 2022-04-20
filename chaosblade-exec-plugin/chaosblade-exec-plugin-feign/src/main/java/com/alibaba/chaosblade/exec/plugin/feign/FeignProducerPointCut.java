package com.alibaba.chaosblade.exec.plugin.feign;

import com.alibaba.chaosblade.exec.common.aop.PointCut;
import com.alibaba.chaosblade.exec.common.aop.matcher.MethodInfo;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.ClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.NameClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.MethodMatcher;

/**
 * @author guoyu486@gmail.com
 */
public class FeignProducerPointCut implements PointCut, FeignConstant {

    @Override
    public ClassMatcher getClassMatcher() {
        return new NameClassMatcher(ENHANCER_CLASS);
    }

    @Override
    public MethodMatcher getMethodMatcher() {
        return new MethodMatcher() {
            @Override
            public boolean isMatched(String methodName, MethodInfo methodInfo) {
                return methodName.equals(METHOD) && methodInfo.getParameterTypes().length == 2;
            }
        };
    }
}
