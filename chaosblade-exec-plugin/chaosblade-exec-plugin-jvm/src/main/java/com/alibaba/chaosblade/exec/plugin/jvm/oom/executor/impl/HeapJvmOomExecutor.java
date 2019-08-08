package com.alibaba.chaosblade.exec.plugin.jvm.oom.executor.impl;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.plugin.jvm.JvmConstant;
import com.alibaba.chaosblade.exec.plugin.jvm.oom.JvmMemoryArea;
import com.alibaba.chaosblade.exec.plugin.jvm.oom.OomObject;
import com.alibaba.chaosblade.exec.plugin.jvm.oom.executor.JvmOomExecutor;

/**
 * @author RinaisSuper
 * @date 2019-04-18
 * @email rinalhb@icloud.com
 */
public class HeapJvmOomExecutor extends JvmOomExecutor {

    private List<OomObject> oomObjects = new ArrayList<OomObject>();

    private Integer MIN_TIME_COST_FILL_HEAP = 3000;

    @Override
    public JvmMemoryArea supportArea() {
        return JvmMemoryArea.HEAP;
    }

    @Override
    protected void innerRun(EnhancerModel enhancerModel, JvmOomConfiguration jvmOomConfiguration) {
        oomObjects.add(new OomObject(jvmOomConfiguration.getBlock()));
    }

    @Override
    public void run(EnhancerModel enhancerModel) throws Exception {
        oomObjects = new ArrayList<OomObject>();
        super.run(enhancerModel);
    }

    @Override
    protected void recycleMemory() {
        this.oomObjects = new ArrayList<OomObject>();
    }

    @Override
    protected Long calculateCostMemoryInterval(JvmOomConfiguration jvmOomConfiguration) {
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        long maxUsed = memoryMXBean.getHeapMemoryUsage().getMax();
        long heapUsed = memoryMXBean.getHeapMemoryUsage().getUsed();
        long left = maxUsed - heapUsed;
        long count = left / (JvmConstant.ONE_MB * jvmOomConfiguration.getBlock());
        long intervalInMillsecond = 0;
        if (count > 1) {
            intervalInMillsecond = MIN_TIME_COST_FILL_HEAP / count;
        }
        return intervalInMillsecond;
    }

    @Override
    protected void innerStop(EnhancerModel enhancerModel) {
        oomObjects = null;
    }

}
