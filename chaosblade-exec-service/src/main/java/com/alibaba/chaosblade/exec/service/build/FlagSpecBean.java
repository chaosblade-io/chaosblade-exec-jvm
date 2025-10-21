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

package com.alibaba.chaosblade.exec.service.build;

import com.alibaba.chaosblade.exec.common.model.FlagSpec;

/** @author Changjun Xiao */
public class FlagSpecBean {
  private String name;
  private String desc;
  private boolean noArgs;
  private boolean required;

  public FlagSpecBean(FlagSpec spec) {
    this.name = spec.getName();
    this.desc = spec.getDesc();
    this.noArgs = spec.noArgs();
    this.required = spec.required();
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDesc() {
    return desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }

  public boolean isNoArgs() {
    return noArgs;
  }

  public void setNoArgs(boolean noArgs) {
    this.noArgs = noArgs;
  }

  public boolean isRequired() {
    return required;
  }

  public void setRequired(boolean required) {
    this.required = required;
  }
}
