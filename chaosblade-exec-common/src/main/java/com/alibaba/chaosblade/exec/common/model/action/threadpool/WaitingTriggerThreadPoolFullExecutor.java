package com.alibaba.chaosblade.exec.common.model.action.threadpool;

/**
 * @author Changjun Xiao
 */
public abstract class WaitingTriggerThreadPoolFullExecutor extends AbstractThreadPoolFullExecutor {

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
    protected synchronized void triggerThreadPoolFull() {
        if (isRunning()) {
            return;
        }
        full(getThreadPoolExecutor());
    }

    @Override
    public void revoke() {
        super.revoke();
        doRevoke();
    }

    /**
     * For releasing resource
     */
    protected abstract void doRevoke();
}
