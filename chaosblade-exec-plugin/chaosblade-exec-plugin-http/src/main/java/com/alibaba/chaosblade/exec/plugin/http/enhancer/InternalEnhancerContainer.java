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

package com.alibaba.chaosblade.exec.plugin.http.enhancer;

import com.alibaba.chaosblade.exec.common.aop.Enhancer;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/** @author shizhi.zhu@quanr.com */
public class InternalEnhancerContainer {
  private Map<String, Enhancer> enhancerMap = new ConcurrentHashMap<String, Enhancer>();

  public void add(Enhancer enhancer) {
    InternalPointCut annotation = enhancer.getClass().getAnnotation(InternalPointCut.class);
    if (annotation != null) {
      enhancerMap.put(join(annotation.className(), annotation.methodName()), enhancer);
    }
  }

  public Enhancer get(String className, String methodName) {
    return enhancerMap.get(join(className, methodName));
  }

  private String join(String className, String methodName) {
    return className + "#" + methodName;
  }
}
