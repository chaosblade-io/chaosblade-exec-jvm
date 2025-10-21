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

package com.alibaba.chaosblade.exec.common.center;

import java.util.*;

public class DefaultSPIServiceManager implements SPIServiceManager {
  private static Map<String, List<Object>> spiMap;

  static {
    spiMap = new HashMap<String, List<Object>>();
  }

  @Override
  public void load() {}

  @Override
  public List<Object> getServices(String className, ClassLoader classLoader) {
    if (spiMap.containsKey(className)) {
      return spiMap.get(className);
    }
    synchronized (this) {
      if (spiMap.containsKey(className)) {
        return spiMap.get(className);
      }
      List<Object> services = loadService(className, classLoader);
      spiMap.put(className, services);
      return services;
    }
  }

  public List<Object> loadService(String className, ClassLoader classLoader) {
    Class clazz;
    try {
      clazz = classLoader.loadClass(className);
    } catch (ClassNotFoundException e) {
      return Collections.EMPTY_LIST;
    }
    ServiceLoader serviceLoader = ServiceLoader.load(clazz, classLoader);
    List<Object> objects = new ArrayList<Object>();
    for (Object object : serviceLoader) {
      objects.add(object);
    }
    return objects;
  }

  @Override
  public void unload() {
    spiMap.clear();
  }
}
