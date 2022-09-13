package com.alibaba.chaosblade.exec.plugin.security;

import com.alibaba.chaosblade.exec.common.aop.PointCut;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.ClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.InterfaceClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.MethodMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.NameMethodMatcher;

/**
 * @author liubin477@163.com
 */
public class SecurityPointCut implements PointCut {

    @Override
    public ClassMatcher getClassMatcher() {
        return new InterfaceClassMatcher(SecurityConstant.CLASS_AuthenticationManager);
    }

    @Override
    public MethodMatcher getMethodMatcher() {
        return new NameMethodMatcher(SecurityConstant.METHOD_AuthenticationManager$authenticate);
    }
}
