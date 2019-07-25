package com.alibaba.chaosblade.exec.plugin.http.httpclient3;

import static com.alibaba.chaosblade.exec.plugin.http.HttpConstant.*;
import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.plugin.http.HttpEnhancer;

import java.lang.reflect.Method;

/**
 * @Author yuhan
 * @package: com.alibaba.chaosblade.exec.plugin.restTemplate
 * @Date 2019-05-08 20:23
 */
public class HttpClient3Enhancer extends HttpEnhancer {



    @Override
    protected void postDoBeforeAdvice(EnhancerModel enhancerModel) {
        enhancerModel.addMatcher(HTTPCLIENT3, "true");
    }

    @Override
    protected String getUrl(Object [] object) throws Exception {
        Object httpMethod = object[1];
        Method method = methodMap.get(getMehodName());
        if (null == method) {
            method = object[1].getClass().getMethod(getURI, null);
            methodMap.put(getMehodName(), method);
        }
        if (null != method) {
            Object invoke = method.invoke(httpMethod, null);
            if (null != invoke) {
                return getUrl(invoke.toString());
            }
        }
        return null;
    }

    public String getMehodName(){
        return HTTPCLIENT3 + getURI;
    }
}
