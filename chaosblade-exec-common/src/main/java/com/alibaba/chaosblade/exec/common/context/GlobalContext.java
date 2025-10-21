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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** @author shizhi.zhu@qunar.com */
public class GlobalContext {
  private static final Logger LOGGER = LoggerFactory.getLogger(GlobalContext.class);
  private static final GlobalContext DEFAULT = new GlobalContext();
  private final Map<String, Object> contextMap = new ConcurrentHashMap<String, Object>();
  private final Timer timer = new HashedWheelTimer();
  private int timeout = 60;
  private int maxSize = 100000;

  public static GlobalContext getDefaultInstance() {
    return DEFAULT;
  }

  public GlobalContext() {}

  public GlobalContext(int timeout, int maxSize) {
    this.timeout = timeout;
    this.maxSize = maxSize;
  }

  public Object put(final String key, Object value) {
    synchronized (contextMap) {
      if (contextMap.size() >= maxSize) {
        LOGGER.error("contextMap size over limit, failed to put in. key={}", key);
        throw new RuntimeException("contextMap size over limit.");
      }
    }
    Object previous = contextMap.put(key, value);
    // Set the timeout to prevent context didn't removed for any reason
    timer.newTimeout(
        new TimerTask() {
          @Override
          public void run(Timeout timeout) {
            remove(key);
          }
        },
        timeout,
        TimeUnit.SECONDS);
    return previous;
  }

  public Object get(String key) {
    return contextMap.get(key);
  }

  public Object remove(String key) {
    return contextMap.remove(key);
  }

  public boolean containsKey(String key) {
    return contextMap.containsKey(key);
  }
}
