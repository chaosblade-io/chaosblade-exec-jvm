package com.alibaba.chaosblade.exec.common.model.action.threadpool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Changjun Xiao
 */
public abstract class WaitingTriggerThreadPoolFullExecutor extends AbstractThreadPoolFullExecutor {

    private static final Logger LOGGER = LoggerFactory.getLogger(WaitingTriggerThreadPoolFullExecutor.class);
    /**
     * Whether the rule of the experiment is received.
     */
    private volatile boolean expReceived;

    public boolean isExpReceived() {
        return expReceived;
    }

    /**
     * Set experiment injection flag to wait to trigger thread pool full.
     *
     * @param expReceived
     */
    public void setExpReceived(boolean expReceived) {
        this.expReceived = expReceived;
    }

    /**
     * Is triggered when a fetch condition is met.
     */
    protected synchronized void triggerThreadPoolFull() throws Exception {
        if (isRunning()) {
            return;
        }
        LOGGER.debug("trigger thread pool full");
        full(getThreadPoolExecutor());
    }

    @Override
    public void revoke() {
        super.revoke();
        doRevoke();
        LOGGER.debug("has revoked thread pool full");
    }

    /**
     * For releasing resource
     */
    protected abstract void doRevoke();
}
