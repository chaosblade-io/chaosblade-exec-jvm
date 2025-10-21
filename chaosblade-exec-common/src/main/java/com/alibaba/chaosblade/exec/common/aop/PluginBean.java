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

import com.alibaba.chaosblade.exec.common.model.ModelSpec;
import com.alibaba.chaosblade.exec.common.plugin.MethodPlugin;

/** @author Changjun Xiao */
public class PluginBean implements Plugin {

  private String name;
  private ModelSpec modelSpec;
  private PointCut pointCut;
  private Enhancer enhancer;
  private boolean isAfterEvent;

  public PluginBean(Plugin plugin) {
    this.name = plugin.getName();
    this.modelSpec = plugin.getModelSpec();
    this.pointCut = new PointCutBean(plugin.getPointCut());
    this.enhancer = plugin.getEnhancer();
    if (plugin instanceof MethodPlugin) {
      this.isAfterEvent = ((MethodPlugin) plugin).isAfterEvent();
    }
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public ModelSpec getModelSpec() {
    return this.modelSpec;
  }

  @Override
  public PointCut getPointCut() {
    return this.pointCut;
  }

  @Override
  public Enhancer getEnhancer() {
    return this.enhancer;
  }

  public boolean isAfterEvent() {
    return isAfterEvent;
  }
}
