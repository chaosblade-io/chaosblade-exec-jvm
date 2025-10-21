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

package com.alibaba.chaosblade.exec.plugin.jvm.oom.executor;

import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.common.model.action.ActionExecutor;
import com.alibaba.chaosblade.exec.common.util.ConfigUtil;
import com.alibaba.chaosblade.exec.plugin.jvm.JvmConstant;
import com.alibaba.chaosblade.exec.plugin.jvm.StoppableActionExecutor;
import com.alibaba.chaosblade.exec.plugin.jvm.oom.JvmMemoryArea;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * jvm oom abstract executor
 *
 * @author RinaisSuper
 * @date 2019-04-18
 * @email rinalhb@icloud.com
 */
public abstract class JvmOomExecutor implements ActionExecutor, StoppableActionExecutor {

  protected AtomicBoolean started = new AtomicBoolean(false);

  protected static final Logger LOGGER = LoggerFactory.getLogger(JvmOomExecutor.class);

  protected ExecutorService executorService;

  /**
   * the jvm area that executor support
   *
   * @return
   */
  public abstract JvmMemoryArea supportArea();

  private boolean isStarted() {
    return this.started.get();
  }

  private OOMExceptionCounter oomExceptionCounter = new OOMExceptionCounter(supportArea());

  protected void handleThrowable(JvmOomConfiguration jvmOomConfiguration, Throwable throwable) {
    oomExceptionCounter.increase();
    LOGGER.info(
        "Find Exception for area:{},exception type:{},message:{}",
        supportArea().name(),
        throwable.getClass().getName(),
        throwable.getMessage());
    if (!jvmOomConfiguration.isWildMode()) {
      long timeLeft = oomExceptionCounter.getTimeBetweenLastOOMTime();
      if (timeLeft < jvmOomConfiguration.getInterval()) {
        recycleMemory();
        try {
          TimeUnit.MILLISECONDS.sleep(jvmOomConfiguration.getInterval() - timeLeft);
        } catch (InterruptedException e) {
        }
      }
    }
  }

  @Override
  public void run(final EnhancerModel enhancerModel) throws Exception {
    if (started.compareAndSet(false, true)) {
      oomExceptionCounter.init();
      final JvmOomConfiguration jvmOomConfiguration = parse(enhancerModel);
      executorService =
          new ThreadPoolExecutor(
              1,
              1,
              0L,
              TimeUnit.MILLISECONDS,
              new LinkedBlockingQueue<Runnable>(),
              new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                  return new Thread(r, "chaosblade-oom-thread");
                }
              });
      executorService.submit(
          new Runnable() {
            @Override
            public void run() {
              Long interval = calculateCostMemoryInterval(jvmOomConfiguration);
              while (isStarted()) {
                try {
                  innerRun(enhancerModel, jvmOomConfiguration);
                  if (interval > 0) {
                    TimeUnit.MILLISECONDS.sleep(interval);
                  }
                } catch (Throwable throwable) {
                  handleThrowable(jvmOomConfiguration, throwable);
                }
              }
            }
          });
    }
  }

  /** recycle memory */
  protected void recycleMemory() {}

  protected Long calculateCostMemoryInterval(JvmOomConfiguration jvmOomConfiguration) {
    return 0L;
  }

  @Override
  public void stop(EnhancerModel enhancerModel) throws Exception {
    if (started.compareAndSet(true, false)) {
      if (executorService == null) {
        return;
      }
      safelyShutdownExecutor(executorService);
      innerStop(enhancerModel);
      oomExceptionCounter.reset();
    }
  }

  private static class OOMExceptionCounter {

    private AtomicInteger count;

    private Long lastCollectTime;

    private JvmMemoryArea jvmMemoryArea;

    public OOMExceptionCounter(JvmMemoryArea jvmMemoryArea) {
      this.count = new AtomicInteger(0);
      this.jvmMemoryArea = jvmMemoryArea;
    }

    public void increase() {
      this.lastCollectTime = System.currentTimeMillis();
      this.count.addAndGet(1);
    }

    public void init() {
      this.count = new AtomicInteger(0);
      this.lastCollectTime = null;
    }

    public void reset() {
      LOGGER.info(
          "create OutOfMemory error stopped,area:{},exception count:{}",
          jvmMemoryArea.name(),
          count.get());
      init();
    }

    public long getTimeBetweenLastOOMTime() {
      return lastCollectTime == null ? 0 : System.currentTimeMillis() - lastCollectTime;
    }
  }

  /**
   * real operation to create oom
   *
   * @param enhancerModel
   */
  protected abstract void innerRun(
      EnhancerModel enhancerModel, JvmOomConfiguration jvmOomConfiguration);

  /**
   * real operation to stop oom
   *
   * @param enhancerModel
   */
  protected abstract void innerStop(EnhancerModel enhancerModel);

  protected void safelyShutdownExecutor(ExecutorService executorService) {
    try {
      executorService.shutdownNow();
    } catch (Throwable throwable) {

    }
  }

  private JvmOomConfiguration parse(EnhancerModel enhancerModel) {
    JvmOomConfiguration jvmOomConfiguration = new JvmOomConfiguration();
    jvmOomConfiguration.setWildMode(
        ConfigUtil.getActionFlag(enhancerModel, JvmConstant.FLAG_NAME_OOM_HAPPEN_MODE, false));
    jvmOomConfiguration.setInterval(
        ConfigUtil.getActionFlag(
            enhancerModel,
            JvmConstant.FLAG_OOM_ERROR_INTERVAL,
            JvmConstant.MIN_OOM_HAPPEN_INTERVAL_IN_MILLS));
    jvmOomConfiguration.setBlock(
        ConfigUtil.getActionFlag(enhancerModel, JvmConstant.FLAG_NAME_OOM_BLOCK, 1));
    return jvmOomConfiguration;
  }

  public static class JvmOomConfiguration {

    public int getBlock() {
      if (block <= 0) {
        block = 1;
      }
      return block;
    }

    public void setBlock(int block) {
      this.block = block;
    }

    public int block;

    public int getInterval() {
      return interval;
    }

    public void setInterval(int interval) {
      this.interval = interval;
    }

    private int interval;

    public boolean isWildMode() {
      return wildMode;
    }

    public void setWildMode(boolean wildMode) {
      this.wildMode = wildMode;
    }

    private boolean wildMode;
  }
}
