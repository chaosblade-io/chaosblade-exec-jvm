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

public interface PluginBeanManager extends ManagerService {
  /**
   * Get experiment model specification
   *
   * @param target
   * @return PluginBean
   */
  PluginBeans getPlugins(String target);

  /**
   * Register plugin bean
   *
   * @param plugin
   */
  void registerPlugin(PluginBean plugin);

  /**
   * Set the load
   *
   * @param plugins
   * @param target
   */
  void setLoad(PluginBeans plugins, String target);
}
