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

package com.alibaba.chaosblade.exec.common.aop.matcher;

/** @author Changjun Xiao */
public class MethodInfo {

  private int access;
  private String name;
  private String[] parameterTypes;
  private String[] throwsTypes;
  private String[] annotations;
  private String desc;

  public MethodInfo(
      int access,
      String name,
      String[] parameterTypes,
      String[] throwsTypes,
      String[] annotations,
      String desc) {
    this.access = access;
    this.name = name;
    this.parameterTypes = parameterTypes;
    this.throwsTypes = throwsTypes;
    this.annotations = annotations;
    this.desc = desc;
  }

  public int getAccess() {
    return access;
  }

  public String getName() {
    return name;
  }

  public String[] getParameterTypes() {
    return parameterTypes;
  }

  public String[] getThrowsTypes() {
    return throwsTypes;
  }

  public String[] getAnnotations() {
    return annotations;
  }
}
