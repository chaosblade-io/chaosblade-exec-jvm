package com.alibaba.chaosblade.exec.plugin.http.resttemplate;

import com.alibaba.chaosblade.exec.common.aop.Enhancer;
import com.alibaba.chaosblade.exec.common.aop.Plugin;
import com.alibaba.chaosblade.exec.common.aop.PointCut;
import com.alibaba.chaosblade.exec.common.model.ModelSpec;
import com.alibaba.chaosblade.exec.plugin.http.HttpPlugin;
import com.alibaba.chaosblade.exec.plugin.http.model.HttpModelSpec;

/**
 * @Author yuhan
 * @package: com.alibaba.chaosblade.exec.plugin.restTemplate
 * @Date 2019-05-10 10:25
 */
public class RestTemplatePlugin extends HttpPlugin {

    @Override
    public String getName() {
        return "rest";
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
