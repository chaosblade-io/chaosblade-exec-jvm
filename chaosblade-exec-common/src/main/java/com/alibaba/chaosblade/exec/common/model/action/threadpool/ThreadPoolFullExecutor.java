package com.alibaba.chaosblade.exec.common.model.action.threadpool;

import java.util.concurrent.ThreadPoolExecutor;

import com.alibaba.chaosblade.exec.common.model.action.ActionExecutor;

/**
 * @author Changjun Xiao
 */
public interface ThreadPoolFullExecutor extends ActionExecutor {

    /**
     * Thread pool full
     *
     * @param threadPoolExecutor
     */
    void full(ThreadPoolExecutor threadPoolExecutor);

    /**
     * Revoke thread pool full experiment
     */
    void revoke();

    /**
     * Get thread pool executor
     *
     * @return
     * @throws Exception
     */
    ThreadPoolExecutor getThreadPoolExecutor() throws Exception;
}
