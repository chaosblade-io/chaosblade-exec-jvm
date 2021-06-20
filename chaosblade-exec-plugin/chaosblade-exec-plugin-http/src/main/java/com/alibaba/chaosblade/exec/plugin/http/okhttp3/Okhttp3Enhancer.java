package com.alibaba.chaosblade.exec.plugin.http.okhttp3;

import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.common.util.ReflectUtil;
import com.alibaba.chaosblade.exec.plugin.http.HttpEnhancer;
import com.alibaba.chaosblade.exec.plugin.http.UrlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.alibaba.chaosblade.exec.plugin.http.HttpConstant.DEFAULT_TIMEOUT;
import static com.alibaba.chaosblade.exec.plugin.http.HttpConstant.OKHTTP3;

/**
 * 具体实现
 *
 * @author pengpj
 * @date 2020-7-13
 */
public class Okhttp3Enhancer extends HttpEnhancer {

    private static final Logger LOGGER = LoggerFactory.getLogger(Okhttp3Enhancer.class);

    private static final String GET_REQUEST = "request";
    private static final String GET_URL = "url";
    private static final String GET_CONNECTION_TIMEOUT = "connectTimeoutMillis";
    private static final String GET_READ_TIMEOUT = "readTimeoutMillis";


    @Override
    protected void postDoBeforeAdvice(EnhancerModel enhancerModel) {
        enhancerModel.addMatcher(OKHTTP3, "true");
    }

    protected int getTimeout(Object instance, Object[] methodArguments) {
        try {
            Object client = ReflectUtil.getFieldValue(instance, "client", false);
            if (client == null) {
                LOGGER.warn("OkHttpClient from Call not found. return default value {}", DEFAULT_TIMEOUT);
                return DEFAULT_TIMEOUT;
            }
            int connectionTimeout = ReflectUtil.invokeMethod(client, GET_CONNECTION_TIMEOUT, new Object[0], false);
            int readTimeout = ReflectUtil.invokeMethod(client, GET_READ_TIMEOUT, new Object[0], false);
            return connectionTimeout + readTimeout;
        } catch (Exception e) {
            LOGGER.warn("Getting timeout from url occurs exception. return default value {}", DEFAULT_TIMEOUT, e);
        }
        return DEFAULT_TIMEOUT;
    }

    /**
     * get url
     */
    @Override
    protected String getUrl(Object realCall, Object[] object) throws Exception {
        Object request = ReflectUtil.invokeMethod(realCall, GET_REQUEST, new Object[0], false);
        if (request == null) {
            LOGGER.warn("okhttp3 Request is null, can not get necessary values.");
            return null;
        }

        Object requestUrl = ReflectUtil.invokeMethod(request, GET_URL, new Object[0], false);
        if (requestUrl == null) {
            LOGGER.warn("okhttp3 Url is null, can not get necessary values.");
            return null;
        }
        String path = UrlUtils.getUrlExcludeQueryParameters(requestUrl.toString());
        LOGGER.info("okhttp3 path : {}", path);
        return path;
    }

}
