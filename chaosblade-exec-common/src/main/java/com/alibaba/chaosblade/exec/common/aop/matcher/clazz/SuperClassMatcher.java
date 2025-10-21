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

/** @author Changjun Xiao */
public class SuperClassMatcher implements ClassMatcher {

  private String className;
  private String superClass;

  public SuperClassMatcher(String className, String superClass) {
    this.className = className;
    this.superClass = superClass;
  }

  public SuperClassMatcher(String superClass) {
    this.superClass = superClass;
  }

  /**
   * @param className 类名，格式：xxx.xxx.xxx
   * @param classInfo 类信息
   * @return 父类匹配，但自身类名与给定的类名不相等，返回 true，否则返回 false
   */
  @Override
  public boolean isMatched(String className, ClassInfo classInfo) {
    return this.superClass.equals(classInfo.getSuperName()) && (!className.equals(this.className));
  }
}
