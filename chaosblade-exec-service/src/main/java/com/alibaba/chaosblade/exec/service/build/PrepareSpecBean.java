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
import com.alibaba.chaosblade.exec.common.model.prepare.PrepareSpec;
import java.util.ArrayList;
import java.util.List;

/** @author Changjun Xiao */
public class PrepareSpecBean {
  private String type;
  private List<FlagSpecBean> flags;
  private boolean required;

  public PrepareSpecBean(PrepareSpec spec) {
    this.type = spec.getType();
    this.flags = createFlags(spec.getFlags());
    this.required = spec.required();
  }

  private List<FlagSpecBean> createFlags(List<FlagSpec> actionFlags) {
    ArrayList<FlagSpecBean> beans = new ArrayList<FlagSpecBean>();
    if (actionFlags == null) {
      return beans;
    }
    for (FlagSpec actionFlag : actionFlags) {
      beans.add(new FlagSpecBean(actionFlag));
    }
    return beans;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public List<FlagSpecBean> getFlags() {
    return flags;
  }

  public void setFlags(List<FlagSpecBean> flags) {
    this.flags = flags;
  }

  public boolean isRequired() {
    return required;
  }

  public void setRequired(boolean required) {
    this.required = required;
  }
}
