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

package com.alibaba.chaosblade.exec.plugin.jvm.oom.executor;

import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.plugin.jvm.JvmConstant;
import com.alibaba.chaosblade.exec.plugin.jvm.StoppableActionExecutor;
import com.alibaba.chaosblade.exec.plugin.jvm.oom.JvmMemoryArea;
import com.alibaba.chaosblade.exec.plugin.jvm.oom.executor.impl.HeapJvmOomExecutor;
import com.alibaba.chaosblade.exec.plugin.jvm.oom.executor.impl.NoHeapJvmOomExecutor;
import com.alibaba.chaosblade.exec.plugin.jvm.oom.executor.impl.OffHeapJvmOomExecutor;
import java.util.ArrayList;
import java.util.List;

/**
 * @author RinaisSuper
 * @date 2019-04-18
 * @email rinalhb@icloud.com
 */
public class JvmOomExecutorFacade implements StoppableActionExecutor {

  private final List<JvmOomExecutor> jvmOomExecutors = new ArrayList<JvmOomExecutor>();

  public JvmOomExecutorFacade() {
    jvmOomExecutors.add(new HeapJvmOomExecutor());
    jvmOomExecutors.add(new NoHeapJvmOomExecutor());
    jvmOomExecutors.add(new OffHeapJvmOomExecutor());
  }

  @Override
  public void run(EnhancerModel enhancerModel) throws Exception {
    selectExecutor(enhancerModel).run(enhancerModel);
  }

  @Override
  public void stop(EnhancerModel enhancerModel) throws Exception {
    selectExecutor(enhancerModel).stop(enhancerModel);
  }

  private JvmOomExecutor selectExecutor(EnhancerModel enhancerModel) throws Exception {
    String area = enhancerModel.getActionFlag(JvmConstant.FLAG_NAME_MEMORY_AREA);
    JvmMemoryArea jvmMemoryArea = JvmMemoryArea.nameOf(area);
    for (JvmOomExecutor jvmOomExecutor : jvmOomExecutors) {
      if (jvmOomExecutor.supportArea().equals(jvmMemoryArea)) {
        return jvmOomExecutor;
      }
    }
    throw new Exception("Illegal jvm error");
  }
}
