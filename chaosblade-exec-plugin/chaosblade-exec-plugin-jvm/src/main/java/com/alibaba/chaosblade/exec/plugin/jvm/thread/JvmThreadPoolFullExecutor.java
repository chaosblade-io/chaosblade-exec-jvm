package com.alibaba.chaosblade.exec.plugin.jvm.thread;

import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.common.model.action.ActionExecutor;
import com.alibaba.chaosblade.exec.common.model.action.threadpool.AbstractThreadPoolFullExecutor;
import com.alibaba.chaosblade.exec.common.model.action.threadpool.NamedThreadFactory;
import com.alibaba.chaosblade.exec.plugin.jvm.JvmConstant;
import com.alibaba.chaosblade.exec.plugin.jvm.StoppableActionExecutor;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author Yuhan Tang
 * @package: com.alibaba.chaosblade.exec.plugin.jvm.thread
 * @Date 2020-11-02 11:09
 */
public class JvmThreadPoolFullExecutor extends AbstractThreadPoolFullExecutor implements ActionExecutor, StoppableActionExecutor {

    private static final ThreadFactory FACTORY = new NamedThreadFactory("ChasoBlade");

    private static final ArrayBlockingQueue<Runnable> QUEUE = new ArrayBlockingQueue<Runnable>(10);

    private static final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 1,
            0, TimeUnit.SECONDS,
            QUEUE,
            FACTORY);

    @Override
    public void run(EnhancerModel enhancerModel) throws Exception {
        int count = Integer.parseInt(enhancerModel.getActionFlag(JvmConstant.ACTION_THREAD_COUNT));
        getThreadPoolExecutor().setCorePoolSize(count);
        getThreadPoolExecutor().setMaximumPoolSize(count);
        super.full(getThreadPoolExecutor());
    }

    @Override
    public void stop(EnhancerModel enhancerModel) throws Exception {
        revoke();
    }

    @Override
    public ThreadPoolExecutor getThreadPoolExecutor() {
        return threadPoolExecutor;
    }
}
