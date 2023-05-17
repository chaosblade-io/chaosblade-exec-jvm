package com.alibaba.chaosblade.exec.common.model.action.threadpool;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Changjun Xiao
 */
public class DefaultThreadPoolFullExecutor extends AbstractThreadPoolFullExecutor {

    public static final String BLADE_MOCK_THREAD_NAME = "ChaosBlade-Mock";

    @Override
    public ThreadPoolExecutor getThreadPoolExecutor() throws Exception {
        return new ThreadPoolExecutor(0, Integer.MAX_VALUE, Integer.MAX_VALUE, TimeUnit.SECONDS, new
            SynchronousQueue<Runnable>(), new NamedThreadFactory(BLADE_MOCK_THREAD_NAME));
    }
}
