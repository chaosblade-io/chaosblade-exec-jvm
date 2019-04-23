package com.alibaba.chaosblade.exec.plugin.jvm.oom.executor.impl;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.plugin.jvm.oom.JvmMemoryArea;
import com.alibaba.chaosblade.exec.plugin.jvm.oom.OomObject;
import com.alibaba.chaosblade.exec.plugin.jvm.oom.executor.JvmOomExecutor;

/**
 * @author haibin
 * @date 2019-04-18
 * @email haibin.lhb@alibaba-inc.com
 */
public class HeapJvmOomExecutor extends JvmOomExecutor {

    private List<OomObject> oomObjects = new ArrayList<OomObject>();

    @Override
    public JvmMemoryArea supportArea() {
        return JvmMemoryArea.HEAP;
    }

    @Override
    protected void innerRun(EnhancerModel enhancerModel) {
        try {
            oomObjects.add(new OomObject());
        } catch (Throwable throwable) {
            handleThrowable(throwable);
        }
    }

    @Override
    public void run(EnhancerModel enhancerModel) throws Exception {
        oomObjects = new ArrayList<OomObject>();
        super.run(enhancerModel);
    }

    @Override
    protected void innerStop(EnhancerModel enhancerModel) {
        oomObjects = null;
    }

}
