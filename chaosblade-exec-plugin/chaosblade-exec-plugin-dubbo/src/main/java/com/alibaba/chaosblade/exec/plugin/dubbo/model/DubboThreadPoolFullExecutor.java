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

package com.alibaba.chaosblade.exec.plugin.dubbo.model;

import com.alibaba.chaosblade.exec.common.model.action.threadpool.WaitingTriggerThreadPoolFullExecutor;
import com.alibaba.chaosblade.exec.common.util.ReflectUtil;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** @author Changjun Xiao */
public class DubboThreadPoolFullExecutor extends WaitingTriggerThreadPoolFullExecutor {

  public static final DubboThreadPoolFullExecutor INSTANCE = new DubboThreadPoolFullExecutor();

  private static final Logger LOGGER = LoggerFactory.getLogger(DubboThreadPoolFullExecutor.class);

  private volatile Object wrappedChannelHandler;
  private volatile ThreadPoolExecutor tripleExecutor;

  private static final String SIDE_KEY = "side";
  private static final String CONSUMER_SIDE = "consumer";

  @Override
  public ThreadPoolExecutor getThreadPoolExecutor() throws Exception {
    if (tripleExecutor != null) {
      return tripleExecutor;
    }
    if (wrappedChannelHandler == null) {
      return null;
    }
    try {
      Object executorService =
          ReflectUtil.invokeMethod(
              wrappedChannelHandler, "getExecutorService", new Object[0], false);
      if (executorService == null) {
        executorService =
            ReflectUtil.invokeMethod(wrappedChannelHandler, "getExecutor", new Object[0], true);
      }
      if (executorService == null) {
        LOGGER.warn("can't get executor service by getExecutor method");
        return null;
      }
      if (ThreadPoolExecutor.class.isInstance(executorService)) {
        return (ThreadPoolExecutor) executorService;
      }
    } catch (Exception e) {
      LOGGER.warn(
          "invoke getExecutorService method of WrappedChannelHandler exception, wrappedChannelHandler class is {}",
          wrappedChannelHandler.getClass().getName(),
          e);
      throw e;
    }
    return null;
  }

  /**
   * Set wrappedChannelHandler for getting threadPoolExecutor object (Dubbo protocol).
   *
   * @param wrappedChannelHandler
   */
  public void setWrappedChannelHandler(Object wrappedChannelHandler) {
    if (isExpReceived() && this.wrappedChannelHandler == null) {
      try {
        Object url = ReflectUtil.invokeMethod(wrappedChannelHandler, "getUrl", new Object[0], true);
        String sideKey =
            ReflectUtil.invokeMethod(url, "getParameter", new Object[] {SIDE_KEY}, true);
        if (LOGGER.isDebugEnabled()) {
          LOGGER.debug("url: {}, sideKey: {}", url, sideKey);
        }
        if (!CONSUMER_SIDE.equalsIgnoreCase(sideKey)) {
          this.wrappedChannelHandler = wrappedChannelHandler;
          triggerThreadPoolFull();
        }
      } catch (Exception e) {
        this.wrappedChannelHandler = null;
        LOGGER.warn("set WrappedChannelHandler exception", e);
      }
    }
  }

  /**
   * Try to capture the thread pool from DefaultExecutorRepository for Triple protocol (Dubbo 3.x).
   * Falls back silently if the Dubbo version doesn't support this API.
   *
   * @param classLoader application classloader
   * @param url Dubbo URL object
   */
  public void trySetTripleExecutor(ClassLoader classLoader, Object url) {
    if (!isExpReceived() || isRunning() || tripleExecutor != null || wrappedChannelHandler != null) {
      return;
    }
    try {
      Object applicationModel =
          ReflectUtil.invokeMethod(url, "getOrDefaultApplicationModel", new Object[0], false);
      if (applicationModel == null) {
        return;
      }
      Class<?> executorRepoClass =
          classLoader.loadClass(
              "org.apache.dubbo.common.threadpool.manager.ExecutorRepository");
      Object executorRepo =
          ReflectUtil.invokeStaticMethod(
              executorRepoClass, "getInstance", new Object[] {applicationModel}, false);
      if (executorRepo == null) {
        return;
      }
      Object executor = ReflectUtil.invokeMethod(executorRepo, "getExecutor", new Object[] {url}, false);
      if (executor instanceof ThreadPoolExecutor) {
        this.tripleExecutor = (ThreadPoolExecutor) executor;
        LOGGER.info("Captured Triple protocol thread pool executor");
        triggerThreadPoolFull();
      } else if (executor instanceof ExecutorService) {
        LOGGER.warn(
            "Triple executor is not a ThreadPoolExecutor, type: {}",
            executor.getClass().getName());
      }
    } catch (ClassNotFoundException e) {
      LOGGER.debug("DefaultExecutorRepository not found, skip Triple thread pool capture");
    } catch (Exception e) {
      LOGGER.debug("Failed to capture Triple thread pool executor", e);
    }
  }

  @Override
  protected void doRevoke() {
    setExpReceived(false);
    wrappedChannelHandler = null;
    tripleExecutor = null;
  }
}
