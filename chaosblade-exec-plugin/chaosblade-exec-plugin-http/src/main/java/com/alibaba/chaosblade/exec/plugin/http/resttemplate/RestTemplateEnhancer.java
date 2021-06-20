package com.alibaba.chaosblade.exec.plugin.http.resttemplate;

import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.common.util.ReflectUtil;
import com.alibaba.chaosblade.exec.plugin.http.HttpConstant;
import com.alibaba.chaosblade.exec.plugin.http.HttpEnhancer;
import com.alibaba.chaosblade.exec.plugin.http.UrlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.alibaba.chaosblade.exec.plugin.http.HttpConstant.DEFAULT_TIMEOUT;

/**
 * @Author yuhan
 * @package: com.alibaba.chaosblade.exec.plugin.restTemplate
 * @Date 2019-05-08 20:23
 */
public class RestTemplateEnhancer extends HttpEnhancer {
    private static final Logger LOGGER = LoggerFactory.getLogger(RestTemplateEnhancer.class);

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
            int connectionTimeout = ReflectUtil.getFieldValue(requestFactory, "connectTimeout", false);
            int readTimeout = ReflectUtil.getFieldValue(requestFactory, "readTimeout", false);
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
