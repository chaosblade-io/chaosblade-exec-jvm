package com.alibaba.chaosblade.exec.common.util;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import com.alibaba.chaosblade.exec.common.model.action.threadpool.NamedThreadFactory;

/**
 * @author Changjun Xiao
 */
public class ThreadUtil {
    public static ScheduledExecutorService createScheduledExecutorService() {
        return Executors.newScheduledThreadPool(1,
            new NamedThreadFactory("ChaosBlade-Executor"));
    }
}
