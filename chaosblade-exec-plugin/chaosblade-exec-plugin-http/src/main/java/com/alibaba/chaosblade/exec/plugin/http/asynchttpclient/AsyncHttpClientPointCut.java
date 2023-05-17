package com.alibaba.chaosblade.exec.plugin.http.asynchttpclient;

import com.alibaba.chaosblade.exec.common.aop.PointCut;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.ClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.InterfaceClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.NameClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.OrClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.MethodMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.NameMethodMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.OrMethodMatcher;

/**
 * @author shizhi.zhu@qunar.com
 */
public class AsyncHttpClientPointCut implements PointCut {
    @Override
    public ClassMatcher getClassMatcher() {
        return new OrClassMatcher().or(new InterfaceClassMatcher("com.ning.http.client.AsyncHandler"))
            .or(new NameClassMatcher("com.ning.http.client.providers.netty.request.NettyRequestSender"))
            .or(new NameClassMatcher("com.ning.http.client.providers.netty.handler.HttpProtocol"));
    }

    @Override
    public MethodMatcher getMethodMatcher() {
        MethodMatcher onStatusReceivedMatcher = new NameMethodMatcher("onStatusReceived");

        MethodMatcher newNettyRequestAndResponseFutureMatcher =
            new NameMethodMatcher("newNettyRequestAndResponseFuture");

        MethodMatcher handleMatcher = new NameMethodMatcher("handle");
        return new OrMethodMatcher().or(onStatusReceivedMatcher).or(newNettyRequestAndResponseFutureMatcher)
            .or(handleMatcher);
    }
}
