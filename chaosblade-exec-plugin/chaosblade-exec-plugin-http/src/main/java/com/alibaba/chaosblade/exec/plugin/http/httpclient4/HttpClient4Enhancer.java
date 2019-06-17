package com.alibaba.chaosblade.exec.plugin.http.httpclient4;

import static com.alibaba.chaosblade.exec.plugin.http.HttpConstant.*;
import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.plugin.http.HttpEnhancer;

import java.lang.reflect.Method;

/**
 * @Author yuhan
 * @package: com.alibaba.chaosblade.exec.plugin.restTemplate
 * @Date 2019-05-08 20:23
 */
public class HttpClient4Enhancer extends HttpEnhancer {

    private static final String prefix = "httpclient4";

    @Override
    protected void postDoBeforeAdvice(EnhancerModel enhancerModel) {
        enhancerModel.addMatcher(HTTPCLIENT4, "true");
    }

    @Override
    protected String getUrl(Object [] object) throws Exception {
        Object httpRequestBase = object[1];
        Method method = methodMap.get(getMehodName());
        if (null == method) {
            method = object[1].getClass().getMethod(getURI, null);
            methodMap.put(getMehodName(), method);
        }
        if (null != method) {
            Object invoke = method.invoke(httpRequestBase, null);
            if (null != invoke) {
                return getUrl(invoke.toString());
            }
        }
        return null;
    }

    public String getMehodName(){
        return HTTPCLIENT4 + getURI;
    }
}
