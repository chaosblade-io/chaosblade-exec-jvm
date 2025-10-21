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

package com.alibaba.chaosblade.exec.common.aop.matcher.method;

import com.alibaba.chaosblade.exec.common.aop.matcher.MethodInfo;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/** @author Changjun Xiao */
public class ParameterMethodMatcher implements MethodMatcher {

  public static final int LESS_THAN = -1;
  public static final int EQUAL = 0;
  public static final int GREAT_THAN = 1;

  public static final int DEFAULT_LENGTH = -1;
  private Map<Integer, String> parametersMap;
  private int parametersLength = DEFAULT_LENGTH;
  private int compareFlag;

  public ParameterMethodMatcher(int parametersLength, int compareFlag) {
    this.parametersLength = parametersLength;
    this.compareFlag = compareFlag;
  }

  public ParameterMethodMatcher(String[] parameters) {
    convertToMap(parameters);
  }

  public ParameterMethodMatcher(String[] parameters, int parametersLength, int compareFlag) {
    this.parametersMap = convertToMap(parameters);
    this.parametersLength = parametersLength;
    this.compareFlag = compareFlag;
  }

  private Map<Integer, String> convertToMap(String[] parameters) {
    HashMap<Integer, String> map = new HashMap<Integer, String>(4);
    for (int i = 0; i < parameters.length; i++) {
      if (parameters[i] != null) {
        map.put(i, parameters[i]);
      }
    }
    return map;
  }

  /**
   * Compare method parameters
   *
   * @param methodName
   * @param methodInfo
   * @return
   */
  @Override
  public boolean isMatched(String methodName, MethodInfo methodInfo) {
    String[] parameterTypes = methodInfo.getParameterTypes();
    int length = parameterTypes.length;

    boolean result = compareParametersLength(length);
    if (!result) {
      return false;
    }

    if (parametersMap == null || parametersMap.isEmpty()) {
      return true;
    }
    Set<Entry<Integer, String>> entries = parametersMap.entrySet();
    for (Entry<Integer, String> entry : entries) {
      int index = entry.getKey();
      if (index >= length) {
        return false;
      }
      if (!parameterTypes[index].equals(entry.getValue())) {
        return false;
      }
    }
    return true;
  }

  /**
   * Compare parameters length
   *
   * @param length
   * @return
   */
  private boolean compareParametersLength(int length) {
    if (parametersLength != DEFAULT_LENGTH) {
      switch (compareFlag) {
        case LESS_THAN:
          if (length >= parametersLength) {
            return false;
          }
          break;
        case EQUAL:
          if (length != parametersLength) {
            return false;
          }
          break;
        case GREAT_THAN:
          if (length <= parametersLength) {
            return false;
          }
          break;
        default:
          return false;
      }
    }
    return true;
  }
}
