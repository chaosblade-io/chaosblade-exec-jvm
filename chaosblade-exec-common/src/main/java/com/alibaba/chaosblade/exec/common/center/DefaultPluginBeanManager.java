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

import com.alibaba.chaosblade.exec.common.aop.PluginBean;
import com.alibaba.chaosblade.exec.common.aop.PluginBeans;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class DefaultPluginBeanManager implements PluginBeanManager {

  private Map<String, PluginBeans> plugins = new HashMap<String, PluginBeans>();

  @Override
  public void load() {}

  @Override
  public void unload() {
    plugins.clear();
  }

  @Override
  public PluginBeans getPlugins(String target) {
    return plugins.get(target);
  }

  @Override
  public void registerPlugin(PluginBean plugin) {
    String target = plugin.getModelSpec().getTarget();
    if (!plugins.containsKey(target)) {
      PluginBeans pluginBeans = new PluginBeans();
      pluginBeans.setLoad(false);
      pluginBeans.setPluginBeans(new HashSet<PluginBean>());
      plugins.put(target, pluginBeans);
    }
    plugins.get(target).getPluginBeans().add(plugin);
  }

  @Override
  public void setLoad(PluginBeans pluginBeans, String target) {
    pluginBeans.setLoad(true);
    plugins.put(target, pluginBeans);
  }
}
