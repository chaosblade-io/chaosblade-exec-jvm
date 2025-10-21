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

package com.alibaba.chaosblade.exec.plugin.hbase;

import com.alibaba.chaosblade.exec.common.aop.Enhancer;
import com.alibaba.chaosblade.exec.common.aop.Plugin;
import com.alibaba.chaosblade.exec.common.aop.PointCut;
import com.alibaba.chaosblade.exec.common.model.ModelSpec;

/**
 * @Author <a href="tangyuhan@shulie.io">yuhan.tang</a>
 *
 * @package: com.alibaba.chaosblade.exec.plugin.hbase @Date 2020-10-30 14:06
 */
public class HbasePlugin implements Plugin {

  @Override
  public String getName() {
    return HbaseConstant.TARGET_NAME;
  }

  @Override
  public ModelSpec getModelSpec() {
    return new HbaseModelSpec();
  }

  @Override
  public PointCut getPointCut() {
    return new HbasePointCut();
  }

  @Override
  public Enhancer getEnhancer() {
    return new HbaseEnhancer();
  }
}
