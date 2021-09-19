package com.alibaba.chaosblade.exec.plugin.http.asynchttpclient;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import com.alibaba.chaosblade.exec.common.constant.ModelConstant;
import com.alibaba.chaosblade.exec.common.util.BusinessParamUtil;
import com.alibaba.chaosblade.exec.plugin.http.HttpEnhancer;
import com.alibaba.chaosblade.exec.spi.BusinessDataGetter;
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
public class HttpProtocolEnhancer extends HttpEnhancer {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpProtocolEnhancer.class);

    @Override
    public EnhancerModel doBeforeAdvice(ClassLoader classLoader, String className, Object object, Method method,
                                        Object[] methodArguments) throws Exception {
        super.doBeforeAdvice(classLoader,className,object,method,methodArguments);
        if (!shouldAddCallPoint()) {
            return null;
        }
        Object headers = getHttpHeader(className, object, method, methodArguments);
        if (headers == null) {
            return null;
        }
        List<String> values = (List<String>) ReflectUtil.invokeMethod(headers, "get", new String[]{HttpConstant.REQUEST_ID});
        if (values != null && !values.isEmpty()) {
            String id = values.get(0);
            StackTraceElement[] stackTrace = (StackTraceElement[]) GlobalContext.getDefaultInstance().remove(id);
            ThreadLocalContext.Content content;
            if (ThreadLocalContext.getInstance().get() == null) {
                content = new ThreadLocalContext.Content();
            } else {
                content = ThreadLocalContext.getInstance().get();
            }
            content.setStackTraceElements(stackTrace);
            ThreadLocalContext.getInstance().set(content);
        } else {
            LOGGER.warn("header not found, className:{}, methodName:{}", className, method.getName());
        }
        return null;
    }

    @Override
    protected Map<String, Map<String, String>> getBusinessParams(String className, Object instance, Method method, Object[] methodArguments) throws Exception {
        if (!shouldAddBusinessParam()) {
            return null;
        }
        final Object headers = getHttpHeader(className, instance, method, methodArguments);
        if (headers == null) {
            return null;
        }
        ThreadLocalContext.Content content;
        if (ThreadLocalContext.getInstance().get() == null) {
            content = new ThreadLocalContext.Content();
        } else {
            content = ThreadLocalContext.getInstance().get();
        }
        content.settValue(BusinessParamUtil.getAndParse(HttpConstant.ASYNC_HTTP_TARGET_NAME, new BusinessDataGetter() {
            @Override
            public String get(String key) throws Exception {
                List<String> values = (List<String>) ReflectUtil.invokeMethod(headers, "get", new Object[]{key}, false);
                if (values != null && !values.isEmpty()) {
                    return values.get(0);
                }
                return null;
            }
        }));
        ThreadLocalContext.getInstance().set(content);
        return null;
    }

    private Object getHttpHeader(String className, Object object, Method method,
                                 Object[] methodArguments) throws Exception {
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
        return headers;
    }


    protected boolean shouldAddCallPoint() {
        return FlagUtil.hasFlag("http", HttpConstant.CALL_POINT_KEY);
    }

    @Override
    protected int getTimeout(Object instance, Object[] methodArguments) {
        return 0;
    }

    @Override
    protected void postDoBeforeAdvice(EnhancerModel enhancerModel) {

    }

    @Override
    protected String getUrl(Object instance, Object[] object) throws Exception {
        return null;
    }

    protected boolean shouldAddBusinessParam() {
        return FlagUtil.hasFlag("http", ModelConstant.BUSINESS_PARAMS);
    }
}
