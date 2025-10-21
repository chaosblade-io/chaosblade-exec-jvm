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

package com.alibaba.chaosblade.exec.plugin.log.log4j2;

import com.alibaba.chaosblade.exec.common.aop.Enhancer;
import com.alibaba.chaosblade.exec.common.aop.PointCut;
import com.alibaba.chaosblade.exec.plugin.log.LogConstant;
import com.alibaba.chaosblade.exec.plugin.log.LogPlugin;

/** @author orion233 */
public class Log4j2Plugin extends LogPlugin {
  @Override
  public String getName() {
    return LogConstant.LOG4J2_KEY;
  }

  @Override
  public PointCut getPointCut() {
    return new Log4j2PointCut();
  }

  @Override
  public Enhancer getEnhancer() {
    return new Log4j2Enhancer();
  }
}
