package com.alibaba.chaosblade.exec.plugin.jvm.oom.executor;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.plugin.jvm.JvmConstant;
import com.alibaba.chaosblade.exec.plugin.jvm.StoppableActionExecutor;
import com.alibaba.chaosblade.exec.plugin.jvm.oom.JvmMemoryArea;
import com.alibaba.chaosblade.exec.plugin.jvm.oom.executor.impl.EmptyOomExecutor;
import com.alibaba.chaosblade.exec.plugin.jvm.oom.executor.impl.HeapJvmOomExecutor;
import com.alibaba.chaosblade.exec.plugin.jvm.oom.executor.impl.NoHeapJvmOomExecutor;
import com.alibaba.chaosblade.exec.plugin.jvm.oom.executor.impl.OffHeapJvmOomExecutor;

/**
 * @author haibin
 * @date 2019-04-18
 * @email haibin.lhb@alibaba-inc.com
 */
public class JvmOomExecutorFacade implements StoppableActionExecutor {

    private final List<JvmOomExecutor> jvmOomExecutors = new ArrayList<JvmOomExecutor>();

    public JvmOomExecutorFacade() {
        jvmOomExecutors.add(new HeapJvmOomExecutor());
        jvmOomExecutors.add(new NoHeapJvmOomExecutor());
        jvmOomExecutors.add(new OffHeapJvmOomExecutor());
        jvmOomExecutors.add(new EmptyOomExecutor());
    }

    @Override
    public void run(EnhancerModel enhancerModel) throws Exception {
        selectExecutor(enhancerModel).run(enhancerModel);
    }

    @Override
    public void stop(EnhancerModel enhancerModel) throws Exception {
        selectExecutor(enhancerModel).stop(enhancerModel);
    }

    private JvmOomExecutor selectExecutor(EnhancerModel enhancerModel) throws Exception {
        String area = enhancerModel.getActionFlag(JvmConstant.FLAG_NAME_MEMORY_AREA);
        JvmMemoryArea jvmMemoryArea = JvmMemoryArea.nameOf(area);
        for (JvmOomExecutor jvmOomExecutor : jvmOomExecutors) {
            if (jvmOomExecutor.supportArea().equals(jvmMemoryArea)) {
                return jvmOomExecutor;
            }
        }
        throw new Exception("Not found jvmOomExecutor for area:" + area);
    }

}
