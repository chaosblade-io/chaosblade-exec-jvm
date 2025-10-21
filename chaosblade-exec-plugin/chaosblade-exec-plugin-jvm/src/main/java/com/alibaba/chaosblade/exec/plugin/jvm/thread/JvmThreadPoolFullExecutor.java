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

package com.alibaba.chaosblade.exec.plugin.jvm.thread;

import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.common.model.action.ActionExecutor;
import com.alibaba.chaosblade.exec.common.util.StringUtil;
import com.alibaba.chaosblade.exec.plugin.jvm.JvmConstant;
import com.alibaba.chaosblade.exec.plugin.jvm.StoppableActionExecutor;
import com.alibaba.chaosblade.exec.plugin.jvm.thread.runstrategy.ThreadRunningStrategy;
import com.alibaba.chaosblade.exec.plugin.jvm.thread.runstrategy.ThreadWaitStrategy;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author Yuhan Tang
 *
 * @package: com.alibaba.chaosblade.exec.plugin.jvm.thread @Date 2020-11-02 11:09
 */
public class JvmThreadPoolFullExecutor implements ActionExecutor, StoppableActionExecutor {

  private static final Logger LOGGER = LoggerFactory.getLogger(JvmThreadPoolFullExecutor.class);

  private final Map<String, ThreadRunStrategy> strategyMap =
      new HashMap<String, ThreadRunStrategy>();

  public JvmThreadPoolFullExecutor() {
    strategyMap.put(JvmConstant.ACTION_THREAD_WAIT, new ThreadWaitStrategy());
    strategyMap.put(JvmConstant.ACTION_THREAD_RUNNING, new ThreadRunningStrategy());
  }

  @Override
  public void run(EnhancerModel enhancerModel) {
    int count = Integer.parseInt(enhancerModel.getActionFlag(JvmConstant.ACTION_THREAD_COUNT));
    if (count < 1) {
      LOGGER.error("error thread count < 1");
      return;
    }
    String strategy = getStrategy(enhancerModel);
    ThreadRunStrategy runStrategy = strategyMap.get(strategy);
    runStrategy.start(count);
  }

  @Override
  public void stop(EnhancerModel enhancerModel) {
    String strategy = getStrategy(enhancerModel);
    ThreadRunStrategy runStrategy = strategyMap.get(strategy);
    runStrategy.stop();
  }

  public String getStrategy(EnhancerModel enhancerModel) {
    String strategy = enhancerModel.getActionFlag(JvmConstant.ACTION_THREAD_WAIT);
    if (!StringUtil.isBlank(strategy)) {
      return JvmConstant.ACTION_THREAD_WAIT;
    }
    strategy = enhancerModel.getActionFlag(JvmConstant.ACTION_THREAD_RUNNING);
    if (!StringUtil.isBlank(strategy)) {
      return JvmConstant.ACTION_THREAD_RUNNING;
    }
    return JvmConstant.ACTION_THREAD_WAIT;
  }
}
