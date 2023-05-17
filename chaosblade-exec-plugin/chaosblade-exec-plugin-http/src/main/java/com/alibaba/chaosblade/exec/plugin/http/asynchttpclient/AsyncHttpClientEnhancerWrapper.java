package com.alibaba.chaosblade.exec.plugin.http.asynchttpclient;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.chaosblade.exec.common.aop.BeforeEnhancer;
import com.alibaba.chaosblade.exec.common.aop.Enhancer;
import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.plugin.http.enhancer.InternalEnhancerContainer;

/**
 * @author shizhi.zhu@qunar.com
 */
public class AsyncHttpClientEnhancerWrapper extends BeforeEnhancer {
    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncHttpClientEnhancerWrapper.class);
    private final InternalEnhancerContainer container = new InternalEnhancerContainer();

    public AsyncHttpClientEnhancerWrapper() {
        container.add(new AsyncHttpClientHandlerEnhancer());
        container.add(new HttpProtocolEnhancer());
        container.add(new NettyRequestSenderEnhancer());
    }

    @Override
    public EnhancerModel doBeforeAdvice(ClassLoader classLoader, String className, Object object, Method method,
                                        Object[] methodArguments) throws Exception {
        Enhancer enhancer = container.get(className, method.getName());
        if (enhancer != null) {
            if (enhancer instanceof BeforeEnhancer) {
                BeforeEnhancer beforeEnhancer = (BeforeEnhancer) enhancer;
                return beforeEnhancer.doBeforeAdvice(classLoader, className, object, method, methodArguments);
            }
        }
        LOGGER.debug("Can't find enhancer for:{}#{}", className, method.getName());
        return null;
    }
}
