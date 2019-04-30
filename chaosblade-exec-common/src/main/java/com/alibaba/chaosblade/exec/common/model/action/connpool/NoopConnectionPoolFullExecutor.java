package com.alibaba.chaosblade.exec.common.model.action.connpool;

import javax.sql.DataSource;

import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.common.exception.ExperimentException;

/**
 * @author Changjun Xiao
 */
public class NoopConnectionPoolFullExecutor implements ConnectionPoolFullExecutor {

    @Override
    public void full(DataSource dataSource, String uniqueName) throws ExperimentException {
        throw new ExperimentException("Not support");
    }

    @Override
    public void revoke(String uniqueName) {
    }

    @Override
    public int getMaxPoolSize(DataSource dataSource) {
        return 0;
    }

    @Override
    public void run(EnhancerModel enhancerModel) throws Exception {
        throw new ExperimentException("Not support");
    }
}
