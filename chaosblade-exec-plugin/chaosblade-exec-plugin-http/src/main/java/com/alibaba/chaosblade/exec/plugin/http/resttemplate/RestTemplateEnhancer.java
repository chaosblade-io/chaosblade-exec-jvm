package com.alibaba.chaosblade.exec.plugin.http.resttemplate;

import static com.alibaba.chaosblade.exec.plugin.http.HttpConstant.DEFAULT_TIMEOUT;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.common.util.ReflectUtil;
import com.alibaba.chaosblade.exec.plugin.http.HttpConstant;
import com.alibaba.chaosblade.exec.plugin.http.HttpEnhancer;
import com.alibaba.chaosblade.exec.plugin.http.UrlUtils;

/**
 * @Author yuhan
 * @package: com.alibaba.chaosblade.exec.plugin.restTemplate
 * @Date 2019-05-08 20:23
 */
public class RestTemplateEnhancer extends HttpEnhancer {
    private static final Logger LOGGER = LoggerFactory.getLogger(RestTemplateEnhancer.class);
    public static final String COMPONENTS_CLIENT_HTTP_REQUEST_FACTORY =
        "org.springframework.http.client.HttpComponentsClientHttpRequestFactory";
    public static final String OK_HTTP_3_CLIENT_HTTP_REQUEST_FACTORY =
        "org.springframework.http.client.OkHttp3ClientHttpRequestFactory";

    @Override
    protected void postDoBeforeAdvice(EnhancerModel enhancerModel) {
        enhancerModel.addMatcher(HttpConstant.REST_KEY, "true");
    }

    @Override
    protected int getTimeout(Object instance, Object[] methodArguments) {
        try {
            Object requestFactory = ReflectUtil.getSuperclassFieldValue(instance, "requestFactory", false);
            if (requestFactory == null) {
                LOGGER.warn("requestFactory from RestTemplate not found. return default value {}", DEFAULT_TIMEOUT);
                return DEFAULT_TIMEOUT;
            }
            int connectionTimeout = 0;
            int readTimeout = 0;
            if (requestFactory.getClass().getName().equalsIgnoreCase(COMPONENTS_CLIENT_HTTP_REQUEST_FACTORY)) {
                Object requestConfig = ReflectUtil.getFieldValue(requestFactory, "requestConfig", false);
                if (requestConfig == null) {
                    LOGGER.warn("config from RequestFactory not found. return default value {}", DEFAULT_TIMEOUT);
                    return DEFAULT_TIMEOUT;
                }
                connectionTimeout = ReflectUtil.getFieldValue(requestConfig, "connectTimeout", false);
                readTimeout = ReflectUtil.getFieldValue(requestConfig, "socketTimeout", false);
            } else if (requestFactory.getClass().getName().equalsIgnoreCase(OK_HTTP_3_CLIENT_HTTP_REQUEST_FACTORY)) {
                Object client = ReflectUtil.getFieldValue(requestFactory, "client", false);
                if (client == null) {
                    LOGGER.warn("client from RequestFactory not found. return default value {}", DEFAULT_TIMEOUT);
                    return DEFAULT_TIMEOUT;
                }
                connectionTimeout = ReflectUtil.getFieldValue(client, "connectTimeout", false);
                readTimeout = ReflectUtil.getFieldValue(client, "readTimeout", false);
            } else {
                connectionTimeout = ReflectUtil.getFieldValue(requestFactory, "connectTimeout", false);
                readTimeout = ReflectUtil.getFieldValue(requestFactory, "readTimeout", false);
            }
            return connectionTimeout + readTimeout;
        } catch (Exception e) {
            LOGGER.warn("Getting timeout from url occurs exception. return default value {}", DEFAULT_TIMEOUT, e);
        }
        return DEFAULT_TIMEOUT;
    }

    @Override
    protected String getUrl(Object instance, Object[] objects) {
        if (null == objects) {
            return null;
        }
        Object object = objects[0];
        if (null == object) {
            return null;
        }
        return UrlUtils.getUrlExcludeQueryParameters(object.toString());
    }
}
