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

package com.alibaba.chaosblade.exec.common.model.action.delay;

import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.common.exception.InterruptProcessException;

/** @author Changjun Xiao */
public abstract class BaseTimeoutExecutor implements TimeoutExecutor {

  protected long timeoutInMillis;
  private ClassLoader classLoader;

  public BaseTimeoutExecutor(ClassLoader classLoader, long timeoutInMillis) {
    this.classLoader = classLoader;
    this.timeoutInMillis = timeoutInMillis;
  }

  @Override
  public long getTimeoutInMillis() {
    return timeoutInMillis;
  }

  @Override
  public void run(EnhancerModel enhancerModel) throws Exception {
    if (timeoutInMillis > 0) {
      Exception exception = generateTimeoutException(classLoader);
      InterruptProcessException.throwThrowsImmediately(
          exception != null ? exception : new Exception("chaosblade mock timeout"));
    }
  }
}
