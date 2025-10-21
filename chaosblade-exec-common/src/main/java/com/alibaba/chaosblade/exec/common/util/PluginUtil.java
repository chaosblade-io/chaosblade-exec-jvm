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

import com.alibaba.chaosblade.exec.common.aop.Plugin;
import com.alibaba.chaosblade.exec.common.constant.DelimiterConstant;

/** @author Changjun Xiao */
public class PluginUtil {

  public static String getIdentifier(Plugin plugin) {
    return plugin.getModelSpec().getTarget()
        + DelimiterConstant.BETWEEN_TARGET_PLUGIN_NAME
        + plugin.getName();
  }

  public static String getIdentifierForAfterEvent(Plugin plugin) {
    return getIdentifier(plugin) + DelimiterConstant.BETWEEN_TARGET_PLUGIN_NAME + "after";
  }
}
