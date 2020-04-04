package com.alibaba.chaosblade.exec.plugin.tars.client;

import com.alibaba.chaosblade.exec.common.aop.Enhancer;
import com.alibaba.chaosblade.exec.common.aop.PointCut;
import com.alibaba.chaosblade.exec.plugin.tars.TarsPlugin;

/**
 * @author saikei
 * @email lishiji@huya.com
 */
public class TarsClientPlugin extends TarsPlugin {
    @Override
    public String getName() {
        return "client";
    }

    @Override
    public PointCut getPointCut() {
        return new TarsClientPointCut();
    }

    @Override
    public Enhancer getEnhancer() {
        return new TarsClientEnhancer();
    }
}
