package com.alibaba.chaosblade.exec.plugin.http.okhttp3;

import com.alibaba.chaosblade.exec.common.aop.Enhancer;
import com.alibaba.chaosblade.exec.common.aop.PointCut;
import com.alibaba.chaosblade.exec.plugin.http.HttpConstant;
import com.alibaba.chaosblade.exec.plugin.http.HttpPlugin;

/**
 * okhttp3 的插件定义
 *
 * @author pengpj
 * @date 2020-7-13
 */
public class Okhttp3Plugin extends HttpPlugin {

    @Override
    public String getName() {
        return HttpConstant.OKHTTP3_TARGET_NAME;
    }

    @Override
    public PointCut getPointCut() {
        return new Okhttp3PointCut();
    }

    @Override
    public Enhancer getEnhancer() {
        return new Okhttp3Enhancer();
    }
}
