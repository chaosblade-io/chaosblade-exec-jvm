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
import java.util.concurrent.ThreadPoolExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** @author Changjun Xiao */
public class DubboThreadPoolFullExecutor extends WaitingTriggerThreadPoolFullExecutor {

  public static final DubboThreadPoolFullExecutor INSTANCE = new DubboThreadPoolFullExecutor();

  private static final Logger LOGGER = LoggerFactory.getLogger(DubboThreadPoolFullExecutor.class);

  private volatile Object wrappedChannelHandler;

  private static final String SIDE_KEY = "side";
  private static final String CONSUMER_SIDE = "consumer";

  @Override
  public ThreadPoolExecutor getThreadPoolExecutor() throws Exception {
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
   * Set wrappedChannelHandler for getting threadPoolExecutor object.
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
        // avoid getting consumer thread pool
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

  @Override
  protected void doRevoke() {
    setExpReceived(false);
    wrappedChannelHandler = null;
  }
}
