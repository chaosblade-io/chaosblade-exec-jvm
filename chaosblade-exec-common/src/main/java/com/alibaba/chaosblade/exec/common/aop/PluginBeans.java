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

package com.alibaba.chaosblade.exec.common.aop;

import java.util.Set;

public class PluginBeans {

  private boolean load;

  private Set<PluginBean> pluginBeans;

  public boolean isLoad() {
    return load;
  }

  public void setLoad(boolean load) {
    this.load = load;
  }

  public Set<PluginBean> getPluginBeans() {
    return pluginBeans;
  }

  public void setPluginBeans(Set<PluginBean> pluginBeans) {
    this.pluginBeans = pluginBeans;
  }
}
