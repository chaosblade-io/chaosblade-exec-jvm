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

package com.alibaba.chaosblade.exec.plugin.jvm.script.java;

/** @author RinaisSuper */
public class JavaUtils {

  public static String getClassName(String scriptContent) {
    int index = scriptContent.indexOf("class");
    if (index == -1) {
      return "";
    }
    char[] chars = scriptContent.toCharArray();
    int startIndex = 0;
    int endIndex = 0;
    for (int i = index + 5; i < scriptContent.toCharArray().length; i++) {
      if (startIndex == 0) {
        if (!isAsciiAlpha(chars[i]) && chars[i] != ' ') {
          break;
        }
        if (isAsciiAlpha(chars[i])) {
          startIndex = i;
          endIndex = i;
        }
      } else {
        if (!(isAsciiAlpha(chars[i]) || isDigital(chars[i]))) {
          endIndex = i;
          break;
        }
      }
    }
    return scriptContent.substring(startIndex, endIndex);
  }

  public static boolean isAsciiAlpha(char ch) {
    return (ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z');
  }

  public static boolean isDigital(char ch) {
    return ch >= '0' && ch <= '9';
  }
}
