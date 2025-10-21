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

/** @author Changjun Xiao */
public class ManagerFactory {

  /** Experiment status manager */
  private static StatusManager statusManager = new DefaultStatusManager();
  /** Experiment model manager */
  private static ModelSpecManager modelSpecManager = new DefaultModelSpecManager();
  /** Plugin model manager */
  private static PluginBeanManager pluginManager = new DefaultPluginBeanManager();
  /** Listener manager manages the plugin listener */
  private static ListenerManager listenerManager = new DefaultListenerManager();

  private static SPIServiceManager spiServiceManager = new DefaultSPIServiceManager();

  public static StatusManager getStatusManager() {
    return statusManager;
  }

  public static ModelSpecManager getModelSpecManager() {
    return modelSpecManager;
  }

  public static PluginBeanManager getPluginManager() {
    return pluginManager;
  }

  public static ListenerManager getListenerManager() {
    return listenerManager;
  }

  public static SPIServiceManager spiServiceManager() {
    return spiServiceManager;
  }

  public static void load() {
    modelSpecManager.load();
    listenerManager.load();
    statusManager.load();
    spiServiceManager.load();
  }

  /** Close manager service */
  public static void unload() {
    statusManager.unload();
    modelSpecManager.unload();
    listenerManager.unload();
    spiServiceManager.unload();
  }
}
