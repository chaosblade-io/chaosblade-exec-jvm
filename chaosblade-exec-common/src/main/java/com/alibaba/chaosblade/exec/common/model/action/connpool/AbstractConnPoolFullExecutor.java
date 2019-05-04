package com.alibaba.chaosblade.exec.common.model.action.connpool;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;

import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.common.util.ThreadUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Changjun Xiao
 */
public abstract class AbstractConnPoolFullExecutor implements ConnectionPoolFullExecutor {

    public static final int DEFAULT_MAX_POOL_SIZE = 100;
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractConnPoolFullExecutor.class);

    private CopyOnWriteArraySet<Connection> connectionHolder = new CopyOnWriteArraySet<Connection>();

    private Object lock = new Object();
    private volatile boolean isRunning;
    private volatile ScheduledExecutorService executorService;

    @Override
    public void run(EnhancerModel enhancerModel) throws Exception {
    }

    @Override
    public void full(final DataSource dataSource) {
        if (dataSource == null) {
            LOGGER.warn("dataSource is null");
            return;
        }
        if (connectionHolder.size() > 0) {
            LOGGER.info("connection pool full has started");
            return;
        }
        isRunning = true;

        int maxPoolSize = getMaxPoolSize();
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
                            connectionHolder.add(connection);
                        }
                    }
                    LOGGER.info("execute connection pool full success");
                } catch (SQLException e) {
                    LOGGER.info("connection pool full, {}", e.getMessage());
                } catch (Exception e) {
                    LOGGER.warn("get database connection exception", e);
                }
            }
        }, 0, 10, TimeUnit.SECONDS);
    }

    @Override
    public void revoke() {
        if (connectionHolder.size() == 0) {
            return;
        }
        executorService.shutdownNow();
        for (Connection connection : connectionHolder) {
            try {
                connection.close();
            } catch (Exception e) {
                LOGGER.warn("Close database connection exception", e);
            }
        }
        connectionHolder.clear();
        isRunning = false;
    }

    public boolean isRunning() {
        return isRunning;
    }
}
