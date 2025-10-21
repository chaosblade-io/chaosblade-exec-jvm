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

/** @author shizhi.zhu@qunar.com */
public class ClassUtils {

  private static final char PACKAGE_SEPARATOR_CHAR = '.';

  public static String simpleClassName(Class<?> clazz) {
    if (clazz == null) {
      throw new NullPointerException("clazz");
    }
    String className = clazz.getName();
    final int lastDotIdx = className.lastIndexOf(PACKAGE_SEPARATOR_CHAR);
    if (lastDotIdx > -1) {
      return className.substring(lastDotIdx + 1);
    }
    return className;
  }
}
