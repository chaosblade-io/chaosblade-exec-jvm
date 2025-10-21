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
public interface ClassMatcher {
  /**
   * 判断是否匹配
   *
   * @param className 类名，格式：xxx.xxx.xxx
   * @param classInfo 类信息
   * @return 匹配返回 true，否则返回 false
   */
  boolean isMatched(String className, ClassInfo classInfo);
}
