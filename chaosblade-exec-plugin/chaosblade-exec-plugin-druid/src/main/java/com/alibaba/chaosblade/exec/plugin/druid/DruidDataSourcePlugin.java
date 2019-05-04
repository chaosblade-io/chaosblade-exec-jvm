package com.alibaba.chaosblade.exec.plugin.druid;

import com.alibaba.chaosblade.exec.common.aop.Enhancer;
import com.alibaba.chaosblade.exec.common.aop.Plugin;
import com.alibaba.chaosblade.exec.common.aop.PointCut;
import com.alibaba.chaosblade.exec.common.model.ModelSpec;

/**
 * @author Changjun Xiao
 */
public class DruidDataSourcePlugin implements Plugin {

    @Override
    public String getName() {
        return DruidConstant.DRUID_DS_PLUGIN_NAME;
    }

    @Override
    public ModelSpec getModelSpec() {
        return new DruidModelSpec();
    }

    @Override
    public PointCut getPointCut() {
        return new DruidDataSourcePointCut();
    }

    @Override
    public Enhancer getEnhancer() {
        return new DruidDataSourceEnhancer();
    }
}
