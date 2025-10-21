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

import com.alibaba.chaosblade.exec.plugin.jvm.thread.ThreadRunStrategy;

/**
 * @Author qianfan
 *
 * @package: com.alibaba.chaosblade.exec.plugin.jvm.thread @Date 2020/12/22 1:19 下午
 */
public class ThreadRunningStrategy implements ThreadRunStrategy {

  private static volatile boolean running = false;

  private Thread[] threads = null;

  @Override
  public void start(int count) {
    running = true;
    threads = new Thread[count];
    for (int i = 0; i < threads.length; i++) {
      threads[i] =
          new Thread(
              new Runnable() {
                @Override
                public void run() {
                  while (running) {}
                }
              });
      threads[i].setName(String.format("ChaosBlade-Thread-%s", i));
      threads[i].start();
    }
  }

  @Override
  public void stop() {
    running = false;
    for (Thread thread : threads) {
      thread = null;
    }
    threads = null;
  }
}
