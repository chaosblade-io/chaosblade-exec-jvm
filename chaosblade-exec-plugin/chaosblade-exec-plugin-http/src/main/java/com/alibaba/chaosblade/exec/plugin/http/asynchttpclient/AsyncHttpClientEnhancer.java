package com.alibaba.chaosblade.exec.plugin.http.asynchttpclient;

import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.common.model.action.delay.TimeoutExecutor;
import com.alibaba.chaosblade.exec.common.util.ReflectUtil;
import com.alibaba.chaosblade.exec.plugin.http.HttpConstant;
import com.alibaba.chaosblade.exec.plugin.http.HttpEnhancer;
import com.alibaba.chaosblade.exec.plugin.http.UrlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketTimeoutException;

import static com.alibaba.chaosblade.exec.plugin.http.HttpConstant.DEFAULT_TIMEOUT;

/**
 * @author shizhi.zhu@qunar.com
 */
public class AsyncHttpClientEnhancer extends HttpEnhancer {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncHttpClientEnhancer.class);
    private static final String GET_CONFIG = "getConfig";
    private static final String GET_CONNECTION_TIMEOUT = "getConnectTimeout";
    private static final String GET_READ_TIMEOUT = "getReadTimeout";

    @Override
    protected void postDoBeforeAdvice(EnhancerModel enhancerModel) {
        enhancerModel.addMatcher(HttpConstant.ASYNC_HTTP_CLIENT, "true");
    }

    @Override
    protected int getTimeout(Object instance, Object[] methodArguments) {
        try {
            if (methodArguments.length < 1) {
                LOGGER.warn("HttpResponseStatus not found. return default value {}", DEFAULT_TIMEOUT);
                return DEFAULT_TIMEOUT;
            }
            Object status = methodArguments[0];
            Object config = ReflectUtil.invokeMethod(status, GET_CONFIG, new Object[0], false);
            if (config == null) {
                LOGGER.warn("AsyncHttpClientConfig from status not found. return default value {}", DEFAULT_TIMEOUT);
                return DEFAULT_TIMEOUT;
            }
            int connectionTimeout = ReflectUtil.invokeMethod(config, GET_CONNECTION_TIMEOUT, new Object[0], false);
            int readTimeout = ReflectUtil.invokeMethod(config, GET_READ_TIMEOUT, new Object[0], false);
            return connectionTimeout + readTimeout;
        } catch (Exception e) {
            LOGGER.warn("Getting timeout from url occurs exception. return default value {}", DEFAULT_TIMEOUT, e);
        }
        return DEFAULT_TIMEOUT;
    }

    protected TimeoutExecutor createTimeoutExecutor(ClassLoader classLoader, final long timeout, String className) {
        return new TimeoutExecutor() {
            @Override
            public long getTimeoutInMillis() {
                return timeout;
            }

            @Override
            public Exception generateTimeoutException(ClassLoader classLoader) {
                return new SocketTimeoutException("Read timed out");
            }

            @Override
            public void run(EnhancerModel enhancerModel) throws Exception {
                // 异步场景不需要在当前线程抛异常
            }
        };
    }

    @Override
    protected String getUrl(Object instance, Object[] object) throws Exception {
        Object httpResponseStatus = object[0];
        Object uri = ReflectUtil.getSuperclassFieldValue(httpResponseStatus, "uri", false);
        String url = ReflectUtil.invokeMethod(uri, "toUrl", new Object[0], false);
        return UrlUtils.getUrlExcludeQueryParameters(url);
    }


}
