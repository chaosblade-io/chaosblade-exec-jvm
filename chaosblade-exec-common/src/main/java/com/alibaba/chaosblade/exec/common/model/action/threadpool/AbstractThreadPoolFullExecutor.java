/*
 * Copyright 2025 The ChaosBlade Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alibaba.chaosblade.exec.common.model.action.threadpool;

import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.common.util.ThreadUtil;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** @author Changjun Xiao */
public abstract class AbstractThreadPoolFullExecutor implements ThreadPoolFullExecutor {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(AbstractThreadPoolFullExecutor.class);

  private CopyOnWriteArraySet<Future> futureCache = new CopyOnWriteArraySet<Future>();
  private volatile boolean isRunning;

  private Object lock = new Object();
  private volatile ScheduledExecutorService executorService;

  @Override
  public void run(EnhancerModel enhancerModel) throws Exception {}

  @Override
  public void full(final ThreadPoolExecutor threadPoolExecutor) {
    if (threadPoolExecutor == null) {
      LOGGER.warn("threadPoolExecutor is null");
      return;
    }
    if (futureCache.size() > 0) {
      LOGGER.info("thread pool has started");
      return;
    }
    isRunning = true;
    final int activeCount = threadPoolExecutor.getActiveCount();
    final int corePoolSize = threadPoolExecutor.getCorePoolSize();
    final int maximumPoolSize = threadPoolExecutor.getMaximumPoolSize();
    LOGGER.info(
        "start execute thread pool full, activeCount: {}, corePoolSize: {}, maximumPoolSize: {}",
        activeCount,
        corePoolSize,
        maximumPoolSize);

    synchronized (lock) {
      if (executorService == null
          || executorService.isShutdown()
          || executorService.isTerminated()) {
        executorService = ThreadUtil.createScheduledExecutorService();
      }
    }
    executorService.scheduleWithFixedDelay(
        new Runnable() {
          @Override
          public void run() {
            try {
              for (int i = 0; i < maximumPoolSize; i++) {
                Future<?> future = threadPoolExecutor.submit(new InterruptableRunnable());
                if (future != null) {
                  futureCache.add(future);
                }
              }
            } catch (RejectedExecutionException e) {
              LOGGER.info("has triggered thread pool full");
            } catch (Exception e) {
              LOGGER.error("execute thread pool full exception", e);
            }
          }
        },
        0,
        10,
        TimeUnit.SECONDS);
  }

  @Override
  public void revoke() {
    if (futureCache.size() == 0) {
      return;
    }
    executorService.shutdownNow();
    for (Future future : futureCache) {
      if (future.isCancelled() || future.isDone()) {
        continue;
      }
      future.cancel(true);
    }
    futureCache.clear();
    isRunning = false;
  }

  public boolean isRunning() {
    return isRunning;
  }
}
