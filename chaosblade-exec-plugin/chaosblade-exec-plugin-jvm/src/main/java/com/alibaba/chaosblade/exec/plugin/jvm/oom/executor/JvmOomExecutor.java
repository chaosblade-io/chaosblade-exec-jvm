package com.alibaba.chaosblade.exec.plugin.jvm.oom.executor;

import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.common.model.action.ActionExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author haibin
 * @date 2019-04-18
 * @email haibin.lhb@alibaba-inc.com
 */
public abstract class JvmOomExecutor implements ActionExecutor {

    protected static final Logger LOGGER = LoggerFactory.getLogger(JvmOomExecutor.class);

    public abstract boolean supportArea(String area);

    public abstract void startInjection();

    public abstract void stopInjection();

    @Override
    public void run(EnhancerModel enhancerModel) throws Exception {
        startInjection();
    }
}
