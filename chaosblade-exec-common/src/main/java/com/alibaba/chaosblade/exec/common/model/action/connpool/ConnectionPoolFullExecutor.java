package com.alibaba.chaosblade.exec.common.model.action.connpool;

import javax.sql.DataSource;

import com.alibaba.chaosblade.exec.common.model.action.ActionExecutor;

/**
 * @author Changjun Xiao
 */
public interface ConnectionPoolFullExecutor extends ActionExecutor {

    /**
     * Trigger connection pool full.
     *
     * @param dataSource
     */
    void full(DataSource dataSource);

    /**
     * Revoke connection pool full.
     *
     */
    void revoke();

    /**
     * Get max pool size from datasource
     *
     * @return
     */
    int getMaxPoolSize();
}
