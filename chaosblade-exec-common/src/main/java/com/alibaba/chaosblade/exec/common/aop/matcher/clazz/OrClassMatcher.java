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

package com.alibaba.chaosblade.exec.common.aop.matcher.clazz;

import com.alibaba.chaosblade.exec.common.aop.matcher.ClassInfo;
import java.util.ArrayList;
import java.util.List;

/** @author Changjun Xiao */
public class OrClassMatcher implements ClassMatcher {

  private List<ClassMatcher> matchers = new ArrayList<ClassMatcher>(2);

  /**
   * Add other class matcher with or relation
   *
   * @param matcher
   * @return
   */
  public OrClassMatcher or(ClassMatcher matcher) {
    if (matcher != null) {
      matchers.add(matcher);
    }
    return this;
  }

  @Override
  public boolean isMatched(String className, ClassInfo classInfo) {
    for (ClassMatcher matcher : matchers) {
      if (matcher.isMatched(className, classInfo)) {
        return true;
      }
    }
    return false;
  }
}
