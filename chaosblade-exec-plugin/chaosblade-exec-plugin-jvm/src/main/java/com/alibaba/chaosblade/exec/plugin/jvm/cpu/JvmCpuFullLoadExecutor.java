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

package com.alibaba.chaosblade.exec.plugin.jvm.cpu;

import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.common.model.action.ActionExecutor;
import com.alibaba.chaosblade.exec.common.util.StringUtil;
import com.alibaba.chaosblade.exec.plugin.jvm.JvmConstant;
import com.alibaba.chaosblade.exec.plugin.jvm.StoppableActionExecutor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** @author Changjun Xiao */
public class JvmCpuFullLoadExecutor implements ActionExecutor, StoppableActionExecutor {

  private static final Logger LOGGER = LoggerFactory.getLogger(JvmCpuFullLoadExecutor.class);

  private volatile ExecutorService executorService;
  private Object lock = new Object();
  private volatile boolean flag;

  /**
   * Through the infinite loop to achieve full CPU load.
   *
   * @param enhancerModel
   * @throws Exception
   */
  @Override
  public void run(EnhancerModel enhancerModel) throws Exception {
    if (executorService != null && (!executorService.isShutdown())) {
      throw new IllegalStateException("The jvm cpu full load experiment is running");
    }
    String cpuCount = enhancerModel.getActionFlag(JvmConstant.FLAG_NAME_CPU_COUNT);
    int maxProcessors = Runtime.getRuntime().availableProcessors();
    int threadCount = maxProcessors;
    if (!StringUtil.isBlank(cpuCount)) {
      Integer count = Integer.valueOf(cpuCount.trim());
      if (count > 0 && count < maxProcessors) {
        threadCount = count;
      }
    }
    synchronized (lock) {
      if (executorService != null && (!executorService.isShutdown())) {
        throw new IllegalStateException("The jvm cpu full load experiment is running...");
      }
      executorService = Executors.newFixedThreadPool(threadCount);
    }
    flag = true;
    for (int i = 0; i < threadCount; i++) {
      executorService.submit(
          new Runnable() {
            @Override
            public void run() {
              while (flag) {}
            }
          });
      LOGGER.info("start jvm cpu full load thread, {}", i);
    }
  }

  @Override
  public void stop(EnhancerModel enhancerModel) throws Exception {
    flag = false;
    if (executorService != null && !executorService.isShutdown()) {
      executorService.shutdownNow();
      executorService = null;
      LOGGER.info("jvm cpu full load stopped");
    }
  }
}
