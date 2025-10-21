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

package com.alibaba.chaosblade.exec.common.context;

import java.util.Map;

/** @author shizhi.zhu@qunar.com */
public class ThreadLocalContext {

  private static ThreadLocalContext DEFAULT = new ThreadLocalContext();
  private InheritableThreadLocal<Content> local = new InheritableThreadLocal<Content>();

  public static ThreadLocalContext getInstance() {
    return DEFAULT;
  }

  public void set(Content value) {
    local.set(value);
  }

  public Content get() {
    return local.get();
  }

  public static class Content {
    private StackTraceElement[] stackTraceElements;
    private Map<String, Map<String, String>> businessData;

    public StackTraceElement[] getStackTraceElements() {
      return stackTraceElements;
    }

    public void setStackTraceElements(StackTraceElement[] stackTraceElements) {
      this.stackTraceElements = stackTraceElements;
    }

    public Map<String, Map<String, String>> getBusinessData() {
      return businessData;
    }

    public void settValue(Map<String, Map<String, String>> businessData) {
      this.businessData = businessData;
    }
  }
}
