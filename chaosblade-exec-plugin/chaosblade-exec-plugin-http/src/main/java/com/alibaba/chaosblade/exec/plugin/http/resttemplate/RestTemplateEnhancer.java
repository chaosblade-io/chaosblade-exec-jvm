package com.alibaba.chaosblade.exec.plugin.http.resttemplate;

import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.plugin.http.HttpConstant;
import com.alibaba.chaosblade.exec.plugin.http.HttpEnhancer;
import com.alibaba.chaosblade.exec.plugin.http.UrlUtils;

/**
 * @Author yuhan
 * @package: com.alibaba.chaosblade.exec.plugin.restTemplate
 * @Date 2019-05-08 20:23
 */
public class RestTemplateEnhancer extends HttpEnhancer {

    @Override
    protected void postDoBeforeAdvice(EnhancerModel enhancerModel) {
        enhancerModel.addMatcher(HttpConstant.REST_KEY, "true");
    }

    @Override
    protected String getUrl(Object [] objects) {
        if(null == objects){
            return null;
        }
        Object object = objects[0];
        if(null == object){
            return null;
        }
        return UrlUtils.getUrlExcludeQueryParameters(object.toString());
    }
}
