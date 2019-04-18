package com.alibaba.chaosblade.exec.plugin.jvm.oom.executor.impl;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.chaosblade.exec.plugin.jvm.oom.JvmMemoryArea;
import com.alibaba.chaosblade.exec.plugin.jvm.oom.OomObject;
import com.alibaba.chaosblade.exec.plugin.jvm.oom.executor.JvmOomExecutor;

/**
 * @author haibin
 * @date 2019-04-18
 * @email haibin.lhb@alibaba-inc.com
 */
public class HeapJvmOomExecutor extends JvmOomExecutor {

    @Override
    public boolean supportArea(String area) {
        return JvmMemoryArea.HEAP.name().equalsIgnoreCase(area);
    }

    @Override
    public void startInjection() {
        List<OomObject> oomObjects = new ArrayList<OomObject>();
        while (true) {
            try {
                oomObjects.add(new OomObject());
            } catch (Throwable throwable) {
                LOGGER.warn("add oom object error", throwable);
            }
        }
    }

    @Override
    public void stopInjection() {

    }

}
