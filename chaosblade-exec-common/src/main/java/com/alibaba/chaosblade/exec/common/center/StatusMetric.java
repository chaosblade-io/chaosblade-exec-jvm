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

package com.alibaba.chaosblade.exec.common.center;

import com.alibaba.chaosblade.exec.common.model.Model;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/** @author Changjun Xiao */
public class StatusMetric {
  private Model model;
  private AtomicLong hitCounts;
  private Lock lock = new ReentrantLock();

  public StatusMetric(Model model) {
    this.model = model;
    this.hitCounts = new AtomicLong(0);
  }

  public Model getModel() {
    return model;
  }

  public void increase() {
    hitCounts.incrementAndGet();
  }

  public void decrease() {
    hitCounts.decrementAndGet();
  }

  public boolean increaseWithLock(long limitCount) {
    if (lock.tryLock()) {
      try {
        if (hitCounts.get() < limitCount) {
          increase();
          return true;
        }
      } finally {
        lock.unlock();
      }
    }
    return false;
  }

  public long getCount() {
    return hitCounts.get();
  }
}
