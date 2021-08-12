package com.alibaba.chaosblade.exec.plugin.http.asynchttpclient;

import java.lang.reflect.Method;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.chaosblade.exec.common.aop.BeforeEnhancer;
import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.common.context.GlobalContext;
import com.alibaba.chaosblade.exec.common.context.ThreadLocalContext;
import com.alibaba.chaosblade.exec.common.util.FlagUtil;
import com.alibaba.chaosblade.exec.common.util.ReflectUtil;
import com.alibaba.chaosblade.exec.plugin.http.HttpConstant;
import com.alibaba.chaosblade.exec.plugin.http.enhancer.InternalPointCut;

/**
 * @author shizhi.zhu@qunar.com
 */
@InternalPointCut(className = "com.ning.http.client.providers.netty.handler.HttpProtocol", methodName = "handle")
public class HttpProtocolEnhancer extends BeforeEnhancer {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpProtocolEnhancer.class);

    @Override
    public EnhancerModel doBeforeAdvice(ClassLoader classLoader, String className, Object object, Method method,
        Object[] methodArguments) throws Exception {
        if (!shouldAddCallPoint()) {
            return null;
        }

        if (methodArguments.length < 2) {
            LOGGER.warn("argument's length less than 2, can't find NettyResponseFuture, className:{}, methodName:{}",
                className, method.getName());
            return null;
        }
        Object future = methodArguments[1];
        if (!future.getClass().getName().equals("com.ning.http.client.providers.netty.future.NettyResponseFuture")) {
            LOGGER.warn("argument is not RequestImpl, className:{}, methodName:{}", className, method.getName());
            return null;
        }
        Object request = ReflectUtil.invokeMethod(future, "getRequest");
        if (request == null) {
            LOGGER.warn("request not found, className:{}, methodName:{}", className, method.getName());
            return null;
        }

        Object headers = ReflectUtil.invokeMethod(request, "getHeaders");
        Object value = ReflectUtil.invokeMethod(headers, "get", new String[] {HttpConstant.REQUEST_ID});
        List<String> values = (List<String>)value;
        if (values == null || values.isEmpty()) {
            LOGGER.warn("header not found, className:{}, methodName:{}", className, method.getName());
            return null;
        }

        String id = values.get(0);
        StackTraceElement[] stackTrace = (StackTraceElement[])GlobalContext.getDefaultInstance().remove(id);
        ThreadLocalContext.getInstance().set(stackTrace);
        return null;
    }

    protected boolean shouldAddCallPoint() {
        return FlagUtil.hasFlag("http", HttpConstant.CALL_POINT_KEY);
    }
}
