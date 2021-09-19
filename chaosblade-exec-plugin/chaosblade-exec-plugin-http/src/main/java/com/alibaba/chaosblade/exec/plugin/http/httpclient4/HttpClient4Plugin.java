package com.alibaba.chaosblade.exec.plugin.http.httpclient4;

import com.alibaba.chaosblade.exec.common.aop.Enhancer;
import com.alibaba.chaosblade.exec.common.aop.PointCut;
import com.alibaba.chaosblade.exec.plugin.http.HttpConstant;
import com.alibaba.chaosblade.exec.plugin.http.HttpPlugin;

/**
 * @Author yuhan
 * @package: com.alibaba.chaosblade.exec.plugin.restTemplate
 * @Date 2019-05-10 10:25
 */
public class HttpClient4Plugin extends HttpPlugin {

    @Override
    public String getName() {
        return HttpConstant.HTTPCLIENT4_TARGET_NAME;
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
