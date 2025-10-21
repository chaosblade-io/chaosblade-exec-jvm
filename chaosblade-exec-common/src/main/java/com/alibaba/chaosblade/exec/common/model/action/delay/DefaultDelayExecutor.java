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
import com.alibaba.chaosblade.exec.common.util.StringUtil;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** @author Changjun Xiao */
public class DefaultDelayExecutor implements DelayExecutor {

  private static final Logger LOGGER = LoggerFactory.getLogger(DefaultDelayExecutor.class);
  private TimeFlagSpec timeFlagSpec;
  private TimeOffsetFlagSpec timeOffsetFlagSpec;

  public DefaultDelayExecutor(TimeFlagSpec timeFlagSpec, TimeOffsetFlagSpec timeOffsetFlagSpec) {
    this.timeFlagSpec = timeFlagSpec;
    this.timeOffsetFlagSpec = timeOffsetFlagSpec;
  }

  @Override
  public void run(EnhancerModel enhancerModel) throws Exception {
    String time = enhancerModel.getActionFlag(timeFlagSpec.getName());
    Integer sleepTimeInMillis = Integer.valueOf(time);
    int offset = 0;
    String offsetTime = enhancerModel.getActionFlag(timeOffsetFlagSpec.getName());
    if (!StringUtil.isBlank(offsetTime)) {
      offset = Integer.valueOf(offsetTime);
    }
    TimeoutExecutor timeoutExecutor = enhancerModel.getTimeoutExecutor();
    if (timeoutExecutor != null) {
      long timeoutInMillis = timeoutExecutor.getTimeoutInMillis();
      if (timeoutInMillis > 0 && timeoutInMillis < sleepTimeInMillis) {
        sleep(timeoutInMillis, 0);
        timeoutExecutor.run(enhancerModel);
        return;
      }
    }
    sleep(sleepTimeInMillis, offset);
  }

  @Override
  public void sleep(long timeInMillis, int offsetInMillis) {
    Random random = new Random();
    int offset = 0;
    if (offsetInMillis > 0) {
      offset = random.nextInt(offsetInMillis);
    }
    if (offset % 2 == 0) {
      timeInMillis = timeInMillis + offset;
    } else {
      timeInMillis = timeInMillis - offset;
    }
    if (timeInMillis <= 0) {
      timeInMillis = offsetInMillis;
    }
    try {
      TimeUnit.MILLISECONDS.sleep(timeInMillis);
    } catch (InterruptedException e) {
      LOGGER.error("running delay action interrupted", e);
    }
  }
}
