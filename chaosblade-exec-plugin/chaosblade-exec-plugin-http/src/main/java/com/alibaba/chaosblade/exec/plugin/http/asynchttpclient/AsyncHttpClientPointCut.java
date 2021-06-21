package com.alibaba.chaosblade.exec.plugin.http.asynchttpclient;

import com.alibaba.chaosblade.exec.common.aop.PointCut;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.ClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.InterfaceClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.MethodMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.NameMethodMatcher;

/**
 * @author shizhi.zhu@qunar.com
 */
public class AsyncHttpClientPointCut implements PointCut {
    @Override
    public ClassMatcher getClassMatcher() {
        return new InterfaceClassMatcher("com.ning.http.client.AsyncHandler");
    }

    @Override
    public MethodMatcher getMethodMatcher() {
        return new NameMethodMatcher("onStatusReceived");
    }
}
