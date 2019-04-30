package com.alibaba.chaosblade.exec.common.model.action.connpool;

import javax.sql.DataSource;

import com.alibaba.chaosblade.exec.common.exception.ExperimentException;
import com.alibaba.chaosblade.exec.common.model.action.ActionExecutor;

/**
 * @author Changjun Xiao
 */
public interface ConnectionPoolFullExecutor extends ActionExecutor {

    void full(DataSource dataSource, String uniqueName) throws ExperimentException;

    void revoke(String uniqueName);

    int getMaxPoolSize(DataSource dataSource);
}
