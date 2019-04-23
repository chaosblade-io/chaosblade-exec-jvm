package com.alibaba.chaosblade.exec.plugin.jvm.oom.executor.impl;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.plugin.jvm.oom.JvmMemoryArea;
import com.alibaba.chaosblade.exec.plugin.jvm.oom.executor.JvmOomExecutor;

/**
 * @author haibin
 * @date 2019-04-18
 * @email haibin.lhb@alibaba-inc.com
 */
public class OffHeapJvmOomExecutor extends JvmOomExecutor {

    private static final int _1MB = 1024 * 1024;

    @Override
    public JvmMemoryArea supportArea() {
        return JvmMemoryArea.OFFHEAP;
    }

    private List<ByteBuffer> oomObjects = new ArrayList<ByteBuffer>();

    @Override
    protected void innerRun(EnhancerModel enhancerModel) {
        try {
            oomObjects.add(ByteBuffer.allocateDirect(20 * _1MB));
        } catch (Throwable throwable) {
            handleThrowable(throwable);
        }
    }

    @Override
    public void run(EnhancerModel enhancerModel) throws Exception {
        oomObjects = new ArrayList<ByteBuffer>();
        super.run(enhancerModel);
    }

    @Override
    protected void innerStop(EnhancerModel enhancerModel) {
        this.oomObjects = null;
    }

}
