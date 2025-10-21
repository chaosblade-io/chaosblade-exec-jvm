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

package com.alibaba.chaosblade.exec.common.aop.matcher.busi;

public enum BusinessParamPatternEnum {
  AND("and", new BusinessParamAndMatcher()),
  OR("or", new BusinessParamOrMatcher()),
  NOT("not", new BusinessParamNotMatcher());

  BusinessParamPatternEnum(String pattern, BusinessParamPatternMatcher patternMatcher) {
    this.pattern = pattern;
    this.patternMatcher = patternMatcher;
  }

  private String pattern;

  private BusinessParamPatternMatcher patternMatcher;

  public String getPattern() {
    return pattern;
  }

  public BusinessParamPatternMatcher getPatternMatcher() {
    return patternMatcher;
  }

  public static BusinessParamPatternMatcher getPatternMatcher(String pattern) {
    for (BusinessParamPatternEnum t : BusinessParamPatternEnum.values()) {
      if (t.getPattern().equalsIgnoreCase(pattern)) {
        return t.getPatternMatcher();
      }
    }
    return null;
  }
}
