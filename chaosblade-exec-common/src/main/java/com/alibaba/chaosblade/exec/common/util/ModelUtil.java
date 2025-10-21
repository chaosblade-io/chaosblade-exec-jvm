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

import com.alibaba.chaosblade.exec.common.model.Model;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherModel;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/** @author Changjun Xiao */
public class ModelUtil {

  public static final String SEPARATOR = "|";

  /**
   * Get the model identifier
   *
   * @param model
   * @return
   */
  public static String getIdentifier(Model model) {
    return getIdentifier(model, model.getActionName());
  }

  public static String getIdentifier(Model model, String action) {
    StringBuilder sb = new StringBuilder();
    String target = model.getTarget();
    sb.append(target).append(SEPARATOR).append(action);

    MatcherModel matcher = model.getMatcher();
    if (matcher != null) {
      Map<String, Object> matchers = matcher.getMatchers();
      TreeMap<String, Object> treeMap = new TreeMap<String, Object>(matchers);
      for (Entry<String, Object> entry : treeMap.entrySet()) {
        sb.append(SEPARATOR).append(entry.getKey()).append("=").append(entry.getValue());
      }
    }
    return sb.toString();
  }

  public static String getTarget(String identifier) {
    return identifier.substring(0, identifier.indexOf(SEPARATOR));
  }
}
