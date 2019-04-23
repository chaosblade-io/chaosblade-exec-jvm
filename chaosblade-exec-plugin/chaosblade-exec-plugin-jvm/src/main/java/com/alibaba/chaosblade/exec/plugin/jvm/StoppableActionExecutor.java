package com.alibaba.chaosblade.exec.plugin.jvm;

import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.common.model.action.ActionExecutor;

/**
 * @author haibin
 * @date 2019-04-23
 */
public interface StoppableActionExecutor extends ActionExecutor {

    /**
     * stop action executor
     *
     * @param enhancerModel
     * @throws Exception
     */
    public void stop(EnhancerModel enhancerModel) throws Exception;
}
