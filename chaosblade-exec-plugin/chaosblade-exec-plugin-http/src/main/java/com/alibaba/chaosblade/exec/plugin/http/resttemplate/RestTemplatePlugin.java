package com.alibaba.chaosblade.exec.plugin.http.resttemplate;

import com.alibaba.chaosblade.exec.common.aop.Enhancer;
import com.alibaba.chaosblade.exec.common.aop.PointCut;
import com.alibaba.chaosblade.exec.plugin.http.HttpConstant;
import com.alibaba.chaosblade.exec.plugin.http.HttpPlugin;

/**
 * @Author yuhan
 * @package: com.alibaba.chaosblade.exec.plugin.restTemplate
 * @Date 2019-05-10 10:25
 */
public class RestTemplatePlugin extends HttpPlugin {

    @Override
    public String getName() {
        return HttpConstant.REST_TARGET_NAME;
    }

    @Override
    public PointCut getPointCut() {
        return new RestTemplatePointCut();
    }

    @Override
    public Enhancer getEnhancer() {
        return new RestTemplateEnhancer();
    }
}
