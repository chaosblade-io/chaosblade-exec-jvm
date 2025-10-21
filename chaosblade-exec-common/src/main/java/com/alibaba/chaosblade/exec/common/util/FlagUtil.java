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

package com.alibaba.chaosblade.exec.common.util;

import com.alibaba.chaosblade.exec.common.center.ManagerFactory;
import com.alibaba.chaosblade.exec.common.center.StatusMetric;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** @author shizhi.zhu@qunar.com */
public class FlagUtil {
  private static final Logger LOGGER = LoggerFactory.getLogger(FlagUtil.class);

  public static boolean hasFlag(String target, String flag) {
    try {
      List<StatusMetric> metrics = ManagerFactory.getStatusManager().getExpByTarget(target);
      for (StatusMetric metric : metrics) {
        Map<String, Object> matchers = metric.getModel().getMatcher().getMatchers();
        if (matchers.containsKey(flag)) {
          return true;
        }
      }
    } catch (Exception e) {
      LOGGER.error("Get flag value from metrics fail, target:{}, flag:{}", target, flag, e);
    }
    return false;
  }
}
