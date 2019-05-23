package com.alibaba.chaosblade.exec.plugin.http.httpclient4;

import com.alibaba.chaosblade.exec.common.aop.Enhancer;
import com.alibaba.chaosblade.exec.common.aop.PointCut;
import com.alibaba.chaosblade.exec.plugin.http.HttpPlugin;
import com.alibaba.chaosblade.exec.plugin.http.resttemplate.RestTemplateEnhancer;
import com.alibaba.chaosblade.exec.plugin.http.resttemplate.RestTemplatePointCut;

/**
 * @Author yuhan
 * @package: com.alibaba.chaosblade.exec.plugin.restTemplate
 * @Date 2019-05-10 10:25
 */
public class HttpClient4Plugin extends HttpPlugin {

    @Override
    public String getName() {
        return "httpclient4";
    }

    @Override
    public PointCut getPointCut() {
        return new HttpClient4PointCut();
    }

    @Override
    public Enhancer getEnhancer() {
        return new HttpClient4Enhancer();
    }
}
