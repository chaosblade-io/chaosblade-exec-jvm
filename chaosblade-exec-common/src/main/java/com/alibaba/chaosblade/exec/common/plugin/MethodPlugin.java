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

package com.alibaba.chaosblade.exec.common.plugin;

import com.alibaba.chaosblade.exec.common.aop.Enhancer;
import com.alibaba.chaosblade.exec.common.aop.Plugin;
import com.alibaba.chaosblade.exec.common.aop.PointCut;
import com.alibaba.chaosblade.exec.common.model.ModelSpec;

/** @author Changjun Xiao */
public class MethodPlugin implements Plugin {

  private PointCut pointCut;
  private String pluginName;
  private boolean afterEvent;

  public MethodPlugin(String pluginName, PointCut pointCut, boolean afterEvent) {
    this.pluginName = pluginName;
    this.pointCut = pointCut;
    this.afterEvent = afterEvent;
  }

  @Override
  public String getName() {
    return pluginName;
  }

  @Override
  public ModelSpec getModelSpec() {
    return new MethodModelSpec();
  }

  @Override
  public PointCut getPointCut() {
    return pointCut;
  }

  @Override
  public Enhancer getEnhancer() {
    return new MethodEnhancer();
  }

  public boolean isAfterEvent() {
    return afterEvent;
  }
}
