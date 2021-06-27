package com.alibaba.chaosblade.exec.plugin.log.logback;

import com.alibaba.chaosblade.exec.common.aop.Enhancer;
import com.alibaba.chaosblade.exec.common.aop.PointCut;
import com.alibaba.chaosblade.exec.plugin.log.LogConstant;
import com.alibaba.chaosblade.exec.plugin.log.LogPlugin;

/**
 * @author shizhi.zhu@qunar.com
 */
public class LogbackPlugin extends LogPlugin {
    @Override
    public String getName() {
        return LogConstant.LOGBACK_KEY;
    }

    @Override
    public PointCut getPointCut() {
        return new LogbackPointCut();
    }

    @Override
    public Enhancer getEnhancer() {
        return new LogbackEnhancer();
    }
}
