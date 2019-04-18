package com.alibaba.chaosblade.exec.plugin.jvm.oom.executor;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.common.model.action.ActionExecutor;
import com.alibaba.chaosblade.exec.plugin.jvm.JvmConstant;
import com.alibaba.chaosblade.exec.plugin.jvm.oom.executor.impl.HeapJvmOomExecutor;
import com.alibaba.chaosblade.exec.plugin.jvm.oom.executor.impl.NoHeapJvmOomExecutor;
import com.alibaba.chaosblade.exec.plugin.jvm.oom.executor.impl.OffHeapJvmOomExecutor;

/**
 * @author haibin
 * @date 2019-04-18
 * @email haibin.lhb@alibaba-inc.com
 */
public class JvmOomExecutorFacade implements ActionExecutor {

    private final List<JvmOomExecutor> jvmOomExecutors = new ArrayList<JvmOomExecutor>();

    public JvmOomExecutorFacade() {
        jvmOomExecutors.add(new HeapJvmOomExecutor());
        jvmOomExecutors.add(new NoHeapJvmOomExecutor());
        jvmOomExecutors.add(new OffHeapJvmOomExecutor());
    }

    @Override
    public void run(EnhancerModel enhancerModel) throws Exception {
        for (JvmOomExecutor jvmOomExecutor : jvmOomExecutors) {
            String area = enhancerModel.getActionFlag(JvmConstant.FLAG_NAME_MEMORY_AREA);

        }
    }
}
