package com.alibaba.chaosblade.exec.plugin.http.httpclient4;

import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.common.util.BusinessParamUtil;
import com.alibaba.chaosblade.exec.common.util.ReflectUtil;
import com.alibaba.chaosblade.exec.plugin.http.HttpConstant;
import com.alibaba.chaosblade.exec.plugin.http.HttpEnhancer;
import com.alibaba.chaosblade.exec.plugin.http.UrlUtils;
import com.alibaba.chaosblade.exec.spi.BusinessDataGetter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Map;

import static com.alibaba.chaosblade.exec.plugin.http.HttpConstant.*;

/**
 * @Author yuhan
 * @package: com.alibaba.chaosblade.exec.plugin.restTemplate
 * @Date 2019-05-08 20:23
 */
public class HttpClient4Enhancer extends HttpEnhancer {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClient4Enhancer.class);

    private static final String GET_PARAMS = "getParams";
    private static final String PARAM_CONFIG = "org.apache.http.client.params.HttpClientParamConfig";
    private static final String GET_REQUEST_CONFIG = "getRequestConfig";
    private static final String REQUEST_GET_CONNECT_TIMEOUT = "getConnectTimeout";
    private static final String REQUEST_GET_SOCKET_TIMEOUT = "getSocketTimeout";
    private static final String GET_CONFIG = "getConfig";
    private static final String GET_FIRST_HEADER = "getFirstHeader";

    private static final String CLIENT_GET_CONNECTION_TIMEOUT = "getConnectionTimeout";
    private static final String CLIENT_GET_SOCKET_TIMEOUT = "getSoTimeout";
    private static final String HTTP_HEADER_GET_VALUE = "getValue";
    private static final String HTTP_CONNECTION_PARAMS = "org.apache.http.params.HttpConnectionParams";


    @Override
    protected void postDoBeforeAdvice(EnhancerModel enhancerModel) {
        enhancerModel.addMatcher(HTTPCLIENT4, "true");
    }

    @Override
    protected Map<String, Map<String, String>> getBusinessParams(String className, Object instance, Method method, final Object[] methodArguments) throws Exception {
        return BusinessParamUtil.getAndParse(HTTPCLIENT4_TARGET_NAME, new BusinessDataGetter() {
            @Override
            public String get(String key) throws Exception {
                Object request = methodArguments[1];
                Object header = ReflectUtil.invokeMethod(request, GET_FIRST_HEADER, new Object[]{key}, false);
                return (String) ReflectUtil.invokeMethod(header, HTTP_HEADER_GET_VALUE, new Object[0], false);
            }
        });
    }

    @Override
    protected int getTimeout(Object instance, Object[] methodArguments) {
        try {
            int connectionTimeout = 0;
            int socketTimeout = 0;
            // 从请求配置中读取
            Object config = null;
            if (methodArguments != null && methodArguments.length >= 2) {
                Object request = methodArguments[1];
                config = ReflectUtil.invokeMethod(request, GET_CONFIG, new Object[0], false);
                if (config == null) {
                    Object params = ReflectUtil.invokeMethod(request, GET_PARAMS, new Object[0], false);
                    Class<?> paramsConfigClass = instance.getClass().getClassLoader().loadClass(PARAM_CONFIG);
                    config = ReflectUtil.invokeStaticMethod(paramsConfigClass, GET_REQUEST_CONFIG, new Object[]{params}, false);
                }
            }
            if (config != null) {
                connectionTimeout = ReflectUtil.invokeMethod(config, REQUEST_GET_CONNECT_TIMEOUT, new Object[0], false);
                socketTimeout = ReflectUtil.invokeMethod(config, REQUEST_GET_SOCKET_TIMEOUT, new Object[0], false);
            }
            if (connectionTimeout > 0 && socketTimeout > 0) {
                return connectionTimeout + socketTimeout;
            }

            // 从client配置中读取
            Object params = ReflectUtil.invokeMethod(instance, GET_PARAMS, new Object[0], false);
            if (params == null) {
                LOGGER.warn("HttpParams from HttpClient not found. return default value {}", DEFAULT_TIMEOUT);
                return DEFAULT_TIMEOUT;
            }
            Class<?> httpConnectionParamsClass = instance.getClass().getClassLoader().loadClass(HTTP_CONNECTION_PARAMS);
            if (connectionTimeout <= 0) {
                connectionTimeout = ReflectUtil.invokeStaticMethod(httpConnectionParamsClass, CLIENT_GET_CONNECTION_TIMEOUT, new Object[]{params}, false);
            }
            if (socketTimeout <= 0) {
                socketTimeout = ReflectUtil.invokeStaticMethod(httpConnectionParamsClass, CLIENT_GET_SOCKET_TIMEOUT, new Object[]{params}, false);
            }
            if (connectionTimeout + socketTimeout == 0) {
                LOGGER.warn("timeout did not config. return default value {}", DEFAULT_TIMEOUT);
                return DEFAULT_TIMEOUT;
            }
            return connectionTimeout + socketTimeout;
        } catch (Exception e) {
            LOGGER.warn("Getting timeout from url occurs exception. return default value {}", DEFAULT_TIMEOUT, e);
        }
        return DEFAULT_TIMEOUT;
    }

    @Override
    protected String getUrl(Object instance, Object[] object) throws Exception {
        Object httpRequestBase = object[1];
        Method method = methodMap.get(getMethodName());
        if (null == method) {
            method = object[1].getClass().getMethod(getURI, null);
            methodMap.put(getMethodName(), method);
        }
        if (null != method) {
            Object invoke = method.invoke(httpRequestBase, null);
            if (null != invoke) {
                return UrlUtils.getUrlExcludeQueryParameters(invoke.toString());
            }
        }
        return null;
    }

    public String getMethodName() {
        return HTTPCLIENT4 + getURI;
    }
}
