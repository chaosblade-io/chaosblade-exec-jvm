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

package com.alibaba.chaosblade.exec.plugin.jvm.codecache;

import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.common.model.action.ActionExecutor;
import com.alibaba.chaosblade.exec.plugin.jvm.StoppableActionExecutor;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jinglei Li
 * @date 2019-07-31
 * @mail liwx2000@gmail.com
 */
public class CodeCacheFillingExecutor implements ActionExecutor, StoppableActionExecutor {

  private static final Logger LOGGER = LoggerFactory.getLogger("CodeCacheFilling");

  private static final String MEMORY_POOL_CODE_CACHE = "Code Cache";

  private static final String COMPILE_THRESHOLD = "-XX:CompileThreshold"; // default: 10000

  private static final int GENERATE_OBJECT_COUNT = 40;

  private final AtomicBoolean started = new AtomicBoolean(false);

  private ExecutorService workers;

  private Thread monitor;

  @Override
  public void run(EnhancerModel enhancerModel) {
    if (started.get() || !started.compareAndSet(false, true)) {
      throw new IllegalStateException(
          "Experiment is running. Please stop the experiment before re-run.");
    }

    if (null != workers && !workers.isShutdown()) {
      workers.shutdown();
      workers = null;
    }

    try {
      int processors = Runtime.getRuntime().availableProcessors();
      int compileThreshold = getCompileThreshold();
      int bucketSize = GENERATE_OBJECT_COUNT / processors;

      CyclicBarrier cyclicBarrier = new CyclicBarrier(processors + 1);

      workers = Executors.newFixedThreadPool(processors);
      monitor = new Thread(new CodeCacheMonitorRunnable(cyclicBarrier, started));

      monitor.start();

      for (int n = 0; n < processors; n++) {
        workers.submit(
            new CodeCacheObjectPreheatingRunnable(
                bucketSize, compileThreshold, cyclicBarrier, started));
      }
    } catch (Throwable e) {
      LOGGER.error("Unknown Error.", e);
      started.compareAndSet(true, false);
    }
  }

  private int getCompileThreshold() {
    int threshold = 10000;

    List<String> arguments = ManagementFactory.getRuntimeMXBean().getInputArguments();
    for (String argument : arguments) {
      if (argument.startsWith(COMPILE_THRESHOLD)) {
        String[] pair = argument.split("=");
        if (pair.length == 2 && null != pair[1] && pair[1].length() > 0) {
          int value = Integer.parseInt(pair[1]);
          if (value > 0) {
            threshold = value;
          }
          break;
        }
      }
    }

    return threshold;
  }

  @Override
  public void stop(EnhancerModel enhancerModel) {
    try {
      if (null != monitor) {
        if (monitor.isAlive()) {
          monitor.interrupt();
        }
        monitor = null;
      }

      if (null != workers) {
        if (!workers.isShutdown()) {
          workers.shutdown();
        }
        workers = null;
      }
    } finally {
      started.set(false);
    }
  }

  static class CodeCacheObjectPreheatingRunnable implements Runnable {

    private final int bucketSize;

    private final int compileThreshold;

    private final CyclicBarrier lock;

    private final AtomicBoolean flag;

    CodeCacheObjectPreheatingRunnable(
        int bucketSize, int compileThreshold, CyclicBarrier lock, AtomicBoolean flag) {
      this.bucketSize = bucketSize;
      this.compileThreshold = compileThreshold;
      this.lock = lock;
      this.flag = flag;
    }

    @Override
    public void run() {
      LOGGER.info("Generating all objects for preheating. Bucket size: " + bucketSize + ".");

      List<Object> objects = new ArrayList<Object>();
      DynamicJavaClassGenerator generator = new DynamicJavaClassGenerator();
      for (int i = 0; i < bucketSize; i++) {
        if (!flag.get()) {
          LOGGER.info("Experiment stopped. Stop code cache object generating.");
        }

        Object object = null;

        try {
          object = generator.generateObject();
        } catch (Exception e) {
          LOGGER.error("Generate CodeCacheObject failed.", e);
        }

        if (null != object) {
          objects.add(object);
        }
      }

      LOGGER.info("Generated all objects for code cache filling.");

      try {
        lock.await();
      } catch (Exception e) {
        LOGGER.error("Worker thread has been interrupted.", e);
        return;
      }

      LOGGER.info("Preheating all objects. Compile threshold: " + compileThreshold + ".");

      while (true) {
        for (Object object : objects) {
          try {
            Method method = object.getClass().getMethod("method");
            for (int i = 0; i <= compileThreshold; i++) {
              if (!flag.get()) {
                LOGGER.info("Experiment stopped. Stop code cache object preheating.");
                return;
              }
              method.invoke(object);
            }
          } catch (Exception e) {
            LOGGER.error("Preheating CodeCacheObject failed.", e);
          }
        }
      }
    }
  }

  static class CodeCacheMonitorRunnable implements Runnable {

    private final CyclicBarrier lock;

    private final AtomicBoolean flag;

    CodeCacheMonitorRunnable(CyclicBarrier lock, AtomicBoolean flag) {
      this.lock = lock;
      this.flag = flag;
    }

    @Override
    public void run() {
      try {
        lock.await();
      } catch (Exception e) {
        LOGGER.error("Monitor thread has been interrupted.", e);
        return;
      }

      int count = 0;
      long latestUsed = 0;

      while (flag.get()) {
        MemoryUsage usage = getCodeCacheUsage();

        if (LOGGER.isDebugEnabled()) {
          LOGGER.debug("Code cache usage: " + usage);
        }

        if (null == usage) {
          LOGGER.warn("Can't get code cache memory usage. Stop code cache monitor.");
          break;
        }

        long used = usage.getUsed();

        if (latestUsed == used) {
          count++;
        } else {
          count = 0;
          latestUsed = used;
        }

        if (count > 5) {
          LOGGER.info("Code cache may be full. Will stop the experiment.");
          flag.compareAndSet(true, false);
        }

        try {
          TimeUnit.SECONDS.sleep(1L);
        } catch (InterruptedException ignored) {
        }
      }
    }

    private MemoryUsage getCodeCacheUsage() {
      List<MemoryPoolMXBean> memoryPoolMXBeans = ManagementFactory.getMemoryPoolMXBeans();
      for (MemoryPoolMXBean memoryPoolMXBean : memoryPoolMXBeans) {
        if (MEMORY_POOL_CODE_CACHE.equals(memoryPoolMXBean.getName())) {
          return memoryPoolMXBean.getUsage();
        }
      }
      return null;
    }
  }
}
