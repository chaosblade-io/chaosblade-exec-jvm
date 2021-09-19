package com.alibaba.chaosblade.exec.plugin.http.asynchttpclient;

import com.alibaba.chaosblade.exec.common.aop.Enhancer;
import com.alibaba.chaosblade.exec.common.aop.PointCut;
import com.alibaba.chaosblade.exec.plugin.http.HttpConstant;
import com.alibaba.chaosblade.exec.plugin.http.HttpPlugin;

/**
 * @author shizhi.zhu@qunar.com
 */
public class AsyncHttpClientPlugin extends HttpPlugin {
    @Override
    public String getName() {
        return HttpConstant.ASYNC_HTTP_TARGET_NAME;
    }

    @Override
    public PointCut getPointCut() {
        return new AsyncHttpClientPointCut();
    }

    @Override
    public Enhancer getEnhancer() {
        return new AsyncHttpClientEnhancerWrapper();
    }
}
