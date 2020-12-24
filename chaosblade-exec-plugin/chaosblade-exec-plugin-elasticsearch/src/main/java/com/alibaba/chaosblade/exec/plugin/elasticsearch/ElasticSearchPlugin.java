package com.alibaba.chaosblade.exec.plugin.elasticsearch;

import com.alibaba.chaosblade.exec.common.aop.Enhancer;
import com.alibaba.chaosblade.exec.common.aop.Plugin;
import com.alibaba.chaosblade.exec.common.aop.PointCut;
import com.alibaba.chaosblade.exec.common.model.ModelSpec;

/**
 * @Author Yuhan Tang
 * @package: com.alibaba.chaosblade.exec.plugin.elasticsearch
 * @Date 2020-10-30 16:27
 */
public class ElasticSearchPlugin implements Plugin {

    @Override
    public String getName() {
        return ElasticSearchConstant.TARGET_NAME;
    }

    @Override
    public ModelSpec getModelSpec() {
        return new ElasticSearchModelSpec();
    }

    @Override
    public PointCut getPointCut() {
        return new ElasticSearchPointCut();
    }

    @Override
    public Enhancer getEnhancer() {
        return new ElasticSearchEnhancer();
    }
}
