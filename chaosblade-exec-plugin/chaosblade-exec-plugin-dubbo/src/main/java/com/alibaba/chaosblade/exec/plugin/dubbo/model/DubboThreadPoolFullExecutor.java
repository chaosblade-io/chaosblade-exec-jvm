package com.alibaba.chaosblade.exec.plugin.dubbo.model;

import java.util.concurrent.ThreadPoolExecutor;

import com.alibaba.chaosblade.exec.common.model.action.threadpool.WaitingTriggerThreadPoolFullExecutor;
import com.alibaba.chaosblade.exec.common.util.ReflectUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Changjun Xiao
 */
public class DubboThreadPoolFullExecutor extends WaitingTriggerThreadPoolFullExecutor {

    public static final DubboThreadPoolFullExecutor INSTANCE = new DubboThreadPoolFullExecutor();

    private static final Logger LOGGER = LoggerFactory.getLogger(DubboThreadPoolFullExecutor.class);

    private volatile Object wrappedChannelHandler;

    @Override
    public ThreadPoolExecutor getThreadPoolExecutor() {
        if (wrappedChannelHandler == null) {
            return null;
        }
        try {
            Object executorService = ReflectUtil.invokeMethod(wrappedChannelHandler, "getExecutorService",
                new Object[0], true);
            if (executorService == null) {
                LOGGER.warn("can get executor service by getExecutorService method");
                return null;
            }
            if (ThreadPoolExecutor.class.isInstance(executorService)) {
                return (ThreadPoolExecutor)executorService;
            }
        } catch (Exception e) {
            LOGGER.warn("invoke getExecutorService method of WrappedChannelHandler exception", e);
        }
        return null;
    }

    /**
     * Set wrappedChannelHandler for getting threadPoolExecutor object.
     *
     * @param wrappedChannelHandler
     */
    public void setWrappedChannelHandler(Object wrappedChannelHandler) {
        if (isExpReceived() && this.wrappedChannelHandler == null) {
            this.wrappedChannelHandler = wrappedChannelHandler;
            triggerThreadPoolFull();
        }
    }

    @Override
    protected void doRevoke() {
        setExpReceived(false);
        wrappedChannelHandler = null;
    }
}
