package com.alibaba.chaosblade.exec.common.model.action.connpool;

import javax.sql.DataSource;

/**
 * @author Changjun Xiao
 */
public abstract class WaitingTriggerConnectionPoolFullExecutor extends AbstractConnPoolFullExecutor {
    protected Object dataSource;
    /**
     * Whether the rule of the experiment is received.
     */
    private volatile boolean expReceived;

    public boolean isExpReceived() {
        return expReceived;
    }

    /**
     * Set experiment injection flag to wait to trigger connection pool full.
     *
     * @param expReceived
     */
    public void setExpReceived(boolean expReceived) {
        this.expReceived = expReceived;
    }

    /**
     * Is triggered when a fetch condition is met.
     */
    protected synchronized void triggerFull() {
        if (isRunning()) {
            return;
        }
        full(getDataSource());
    }

    @Override
    public void revoke() {
        super.revoke();
        doRevoke();
        dataSource = null;
        setExpReceived(false);
    }

    /**
     * For releasing resource
     */
    protected abstract void doRevoke();

    public DataSource getDataSource() {
        if (dataSource != null && DataSource.class.isInstance(dataSource)) {
            return (DataSource)dataSource;
        }
        return null;
    }

    public void setDataSource(Object dataSource) {
        if (isExpReceived() && this.dataSource == null) {
            this.dataSource = dataSource;
            triggerFull();
        }
    }
}
