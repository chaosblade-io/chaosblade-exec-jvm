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

package com.alibaba.chaosblade.exec.plugin.jvm.oom.executor.impl;

import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.plugin.jvm.JvmConstant;
import com.alibaba.chaosblade.exec.plugin.jvm.oom.JvmMemoryArea;
import com.alibaba.chaosblade.exec.plugin.jvm.oom.executor.JvmOomExecutor;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * @author RinaisSuper
 * @date 2019-04-18
 * @email rinalhb@icloud.com
 */
public class OffHeapJvmOomExecutor extends JvmOomExecutor {

  @Override
  public JvmMemoryArea supportArea() {
    return JvmMemoryArea.OFFHEAP;
  }

  private List<ByteBuffer> oomObjects = new ArrayList<ByteBuffer>();

  @Override
  protected void innerRun(EnhancerModel enhancerModel, JvmOomConfiguration jvmOomConfiguration) {
    oomObjects.add(ByteBuffer.allocateDirect(jvmOomConfiguration.getBlock() * JvmConstant.ONE_MB));
  }

  @Override
  public void run(EnhancerModel enhancerModel) throws Exception {
    oomObjects = new ArrayList<ByteBuffer>();
    super.run(enhancerModel);
  }

  @Override
  protected void recycleMemory() {
    this.oomObjects = new ArrayList<ByteBuffer>();
  }

  @Override
  protected void innerStop(EnhancerModel enhancerModel) {
    this.oomObjects = null;
  }
}
