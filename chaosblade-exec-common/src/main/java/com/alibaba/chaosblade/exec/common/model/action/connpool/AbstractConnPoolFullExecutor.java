package com.alibaba.chaosblade.exec.common.model.action.connpool;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;

import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.common.exception.ExperimentException;
import com.alibaba.chaosblade.exec.common.util.ThreadUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Changjun Xiao
 */
public abstract class AbstractConnPoolFullExecutor implements ConnectionPoolFullExecutor {
    public static final int DEFAULT_MAX_POOL_SIZE = 100;
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractConnPoolFullExecutor.class);
    protected ConcurrentHashMap<String, List<Connection>> connectionHolder
        = new ConcurrentHashMap<String, List<Connection>>();
    private volatile ScheduledExecutorService executorService;
    private Object lock = new Object();

    @Override
    public void run(EnhancerModel enhancerModel) throws Exception {
    }

    @Override
    public void full(final DataSource dataSource, String uniqueName) throws ExperimentException {
        if (dataSource == null) {
            LOGGER.warn("dataSource is null");
            return;
        }
        final List<Connection> connectionList = getOrCreateConnectionList(uniqueName);
        int maxPoolSize = getMaxPoolSize(dataSource);
        final int poolSize = maxPoolSize <= 0 ? DEFAULT_MAX_POOL_SIZE : maxPoolSize;
        LOGGER.info("Start execute connection pool full, poolSize: {}", poolSize);
        synchronized (lock) {
            if (executorService == null || executorService.isShutdown() || executorService.isTerminated()) {
                executorService = ThreadUtil.createScheduledExecutorService();
            }
        }
        executorService.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < poolSize; i++) {
                        Connection connection = dataSource.getConnection();
                        if (connection != null) {
                            connectionList.add(connection);
                        }
                    }
                    LOGGER.info("Execute connection pool full success");
                } catch (Exception e) {
                    LOGGER.warn("Get database connection exception", e);
                }
            }
        }, 0, 10, TimeUnit.SECONDS);
    }

    @Override
    public void revoke(String uniqueName) {
        List<Connection> connections = connectionHolder.get(uniqueName);
        if (connections == null) {
            return;
        }
        executorService.shutdownNow();
        for (Connection connection : connections) {
            try {
                connection.close();
            } catch (Exception e) {
                LOGGER.warn("Close database connection exception", e);
            }
        }
        connectionHolder.remove(uniqueName);
    }

    protected List<Connection> getOrCreateConnectionList(String uniqueName) {
        List<Connection> connections = connectionHolder.get(uniqueName);
        if (connections == null) {
            connections = new ArrayList<Connection>();
        }
        List<Connection> oldConnectionList = connectionHolder.putIfAbsent(uniqueName, connections);
        return oldConnectionList == null ? connections : oldConnectionList;
    }
}
