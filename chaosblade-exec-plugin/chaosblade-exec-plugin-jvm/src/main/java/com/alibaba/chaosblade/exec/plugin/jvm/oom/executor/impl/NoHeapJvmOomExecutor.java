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
import com.alibaba.chaosblade.exec.plugin.jvm.oom.JvmMemoryArea;
import com.alibaba.chaosblade.exec.plugin.jvm.oom.OomObject;
import com.alibaba.chaosblade.exec.plugin.jvm.oom.executor.JvmOomExecutor;
import java.lang.reflect.Method;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * @author RinaisSuper
 * @date 2019-04-18
 * @email rinalhb@icloud.com
 */
public class NoHeapJvmOomExecutor extends JvmOomExecutor {
  @Override
  public JvmMemoryArea supportArea() {
    return JvmMemoryArea.NOHEAP;
  }

  @Override
  public void stop(EnhancerModel enhancerModel) throws Exception {
    started.compareAndSet(true, false);
  }

  @Override
  protected void innerRun(EnhancerModel enhancerModel, JvmOomConfiguration jvmOomConfiguration) {
    Enhancer enhancer = new Enhancer();
    enhancer.setSuperclass(OomObject.class);
    enhancer.setUseCache(false);
    enhancer.setCallback(
        new MethodInterceptor() {
          @Override
          public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy)
              throws Throwable {
            return proxy.invokeSuper(obj, args);
          }
        });
    enhancer.create();
  }

  @Override
  protected void innerStop(EnhancerModel enhancerModel) {}
}
