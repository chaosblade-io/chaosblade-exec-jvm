package com.alibaba.chaosblade.exec.plugin.jvm.oom.executor.impl;

import com.alibaba.chaosblade.exec.plugin.jvm.oom.JvmMemoryArea;
import com.alibaba.chaosblade.exec.plugin.jvm.oom.executor.JvmOomExecutor;

/**
 * @author haibin
 * @date 2019-04-18
 * @email haibin.lhb@alibaba-inc.com
 */
public class OffHeapJvmOomExecutor extends JvmOomExecutor {
    @Override
    public boolean supportArea(String area) {
        return JvmMemoryArea.OFFHEAP.name().equalsIgnoreCase(area);
    }

    @Override
    public void startInjection() {

    }

    @Override
    public void stopInjection() {

    }

}
