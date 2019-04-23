package com.alibaba.chaosblade.exec.plugin.jvm.oom.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.common.model.action.ActionExecutor;
import com.alibaba.chaosblade.exec.plugin.jvm.JvmConstant;
import com.alibaba.chaosblade.exec.plugin.jvm.StoppableActionExecutor;
import com.alibaba.chaosblade.exec.plugin.jvm.oom.JvmMemoryArea;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * jvm oom abstract executor
 *
 * @author haibin
 * @date 2019-04-18
 * @email haibin.lhb@alibaba-inc.com
 */
public abstract class JvmOomExecutor implements ActionExecutor, StoppableActionExecutor {

    protected AtomicBoolean started = new AtomicBoolean(false);

    protected static final Logger LOGGER = LoggerFactory.getLogger(JvmOomExecutor.class);

    protected ExecutorService executorService;

    /**
     * the thread count to cause oom
     */
    protected int DEFAULT_THREAD_COUNT = 1;

    /**
     * the jvm area that executor support
     *
     * @return
     */
    public abstract JvmMemoryArea supportArea();

    private boolean isStarted() {
        return this.started.get();
    }

    protected void handleThrowable(Throwable throwable) {
        LOGGER.info("Find Exception for area:{},exception type:{},message:{}", supportArea().name(),
            throwable.getClass().getName(),
            throwable.getMessage());
    }

    @Override
    public void run(final EnhancerModel enhancerModel) throws Exception {
        if (started.compareAndSet(false, true)) {
            executorService = Executors.newFixedThreadPool(threadCount());
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    while (isStarted()) {
                        innerRun(enhancerModel);
                    }
                }
            });
        }
    }

    @Override
    public void stop(EnhancerModel enhancerModel) throws Exception {
        if (started.compareAndSet(true, false)) {
            if (executorService == null) { return; }
            safelyShutdownExecutor(executorService);
            innerStop(enhancerModel);
            if (enableSystemGc(enhancerModel)) {
                LOGGER.info("invoke  System.gc() after stop injection");
                System.gc();
            }
        }
    }

    private boolean enableSystemGc(EnhancerModel enhancerModel) {
        String flag = enhancerModel.getActionFlag(JvmConstant.FLAG_NAME_ENABLE_SYSTEM_GC);
        if (flag == null) { return true; }
        return Boolean.valueOf(flag);
    }

    /**
     * real operation to create oom
     *
     * @param enhancerModel
     */
    protected abstract void innerRun(EnhancerModel enhancerModel);

    /**
     * real operation to stop oom
     *
     * @param enhancerModel
     */
    protected abstract void innerStop(EnhancerModel enhancerModel);

    /**
     * thread count to create oom
     *
     * @return
     */
    protected int threadCount() {
        return DEFAULT_THREAD_COUNT;
    }

    protected void safelyShutdownExecutor(ExecutorService executorService) {
        try {
            executorService.shutdownNow();
        } catch (Throwable throwable) {

        }
    }

}
