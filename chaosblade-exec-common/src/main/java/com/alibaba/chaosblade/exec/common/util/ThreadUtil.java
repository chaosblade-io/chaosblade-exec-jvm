package com.alibaba.chaosblade.exec.common.util;

import com.alibaba.chaosblade.exec.common.model.action.threadpool.NamedThreadFactory;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/** @author Changjun Xiao */
public class ThreadUtil {
  public static ScheduledExecutorService createScheduledExecutorService() {
    return Executors.newScheduledThreadPool(1, new NamedThreadFactory("ChaosBlade-Executor"));
  }
}
