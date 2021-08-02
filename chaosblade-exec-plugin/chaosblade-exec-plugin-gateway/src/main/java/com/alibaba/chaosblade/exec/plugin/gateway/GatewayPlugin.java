package com.alibaba.chaosblade.exec.plugin.gateway;

import com.alibaba.chaosblade.exec.common.aop.Enhancer;
import com.alibaba.chaosblade.exec.common.aop.Plugin;
import com.alibaba.chaosblade.exec.common.aop.PointCut;
import com.alibaba.chaosblade.exec.common.model.ModelSpec;

/**
 * @Author wb-shd671576
 * @package: com.alibaba.chaosblade.exec.plugin.gateway
 * @Date 2021-07-29
 */
public class GatewayPlugin implements Plugin {

    @Override
    public String getName() {
        return GatewayConstant.TARGET_NAME;
    }

    @Override
    public ModelSpec getModelSpec() {
        return new GatewayModelSpec();
    }

    @Override
    public PointCut getPointCut() {
        return new GatewayPointCut();
    }

    @Override
    public Enhancer getEnhancer() {
        return new GatewayEnhancer();
    }
}
