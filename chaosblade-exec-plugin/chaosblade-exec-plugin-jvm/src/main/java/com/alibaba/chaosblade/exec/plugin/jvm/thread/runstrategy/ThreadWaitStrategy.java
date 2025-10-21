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

package com.alibaba.chaosblade.exec.plugin.jvm.thread.runstrategy;

import com.alibaba.chaosblade.exec.common.model.action.threadpool.WaitThread;
import com.alibaba.chaosblade.exec.plugin.jvm.thread.ThreadRunStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author qianfan
 *
 * @package: com.alibaba.chaosblade.exec.plugin.jvm.thread @Date 2020/12/22 1:14 下午
 */
public class ThreadWaitStrategy implements ThreadRunStrategy {

  private static final Logger LOGGER = LoggerFactory.getLogger(ThreadWaitStrategy.class);

  private static final Thread[][] threads = new Thread[1][];

  @Override
  public void start(int count) {
    if (null != threads[0]) {
      LOGGER.warn("threads index 0 is not null");
      return;
    }
    WaitThread[] waitThreads = new WaitThread[count];
    for (int i = 0; i < waitThreads.length; i++) {
      waitThreads[i] = new WaitThread();
      waitThreads[i].setName(String.format("ChaosBlade-Thread-%s", i));
      waitThreads[i].start();
    }
    threads[0] = waitThreads;
  }

  @Override
  public void stop() {
    WaitThread[] waitThreads = (WaitThread[]) threads[0];
    if (waitThreads == null || waitThreads.length < 1) {
      LOGGER.warn("threads index 0 is null");
      return;
    }

    for (int i = 0; i < waitThreads.length; i++) {
      waitThreads[i].unlock();
      waitThreads[i] = null;
    }
    threads[0] = null;
  }
}
