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

import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;

/**
 * @author haibin
 * @date 2019-07-03
 */
public class ConfigUtil {

  public static String getActionFlag(
      EnhancerModel enhancerModel, String flagKey, String defaultFlagValue) {
    String value = enhancerModel.getActionFlag(flagKey);
    if (value == null) {
      return defaultFlagValue;
    }
    return value;
  }

  public static Integer getActionFlag(
      EnhancerModel enhancerModel, String flagKey, Integer defaultFlagValue) {
    String value = getActionFlag(enhancerModel, flagKey);
    if (value == null) {
      return defaultFlagValue;
    }
    try {
      return Integer.valueOf(value);
    } catch (Exception ex) {
      return defaultFlagValue;
    }
  }

  public static Boolean getActionFlag(
      EnhancerModel enhancerModel, String flagKey, Boolean defaultValue) {
    String value = getActionFlag(enhancerModel, flagKey);
    if (value == null) {
      return defaultValue;
    }
    try {
      return Boolean.parseBoolean(value);
    } catch (Exception ex) {
      return defaultValue;
    }
  }

  public static String getActionFlag(EnhancerModel enhancerModel, String flagKey) {
    return enhancerModel.getActionFlag(flagKey);
  }
}
