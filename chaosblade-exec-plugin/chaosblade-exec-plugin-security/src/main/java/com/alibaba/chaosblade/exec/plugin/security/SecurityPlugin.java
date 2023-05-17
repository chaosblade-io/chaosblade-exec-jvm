package com.alibaba.chaosblade.exec.plugin.security;

import com.alibaba.chaosblade.exec.common.aop.Enhancer;
import com.alibaba.chaosblade.exec.common.aop.Plugin;
import com.alibaba.chaosblade.exec.common.aop.PointCut;
import com.alibaba.chaosblade.exec.common.model.ModelSpec;

/**
 * @author liubin@njzfit.cn
 */
public class SecurityPlugin implements Plugin {

    @Override
    public String getName() {
        return SecurityConstant.PLUGIN_NAME;
    }

    @Override
    public ModelSpec getModelSpec() {
        return new SecurityModelSpec();
    }

    @Override
    public PointCut getPointCut() {
        return new SecurityPointCut();
    }

    @Override
    public Enhancer getEnhancer() {
        return new SecurityEnhancer();
    }
}
