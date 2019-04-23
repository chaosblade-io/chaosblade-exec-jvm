package com.alibaba.chaosblade.exec.plugin.jvm.oom.executor.impl;

import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.plugin.jvm.oom.JvmMemoryArea;
import com.alibaba.chaosblade.exec.plugin.jvm.oom.executor.JvmOomExecutor;

/**
 * @author haibin
 * @date 2019-04-23
 */
public class EmptyOomExecutor extends JvmOomExecutor {
    @Override
    public JvmMemoryArea supportArea() {
        return JvmMemoryArea.EMPTY;
    }

    @Override
    protected void innerRun(EnhancerModel enhancerModel) {

    }

    @Override
    protected void innerStop(EnhancerModel enhancerModel) {

    }

}
