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

package com.alibaba.chaosblade.exec.plugin.jvm.script.model;

import java.net.URL;
import java.net.URLClassLoader;

/** @author Changjun Xiao */
public class ClassLoaderForScript extends URLClassLoader {
  private ClassLoader pluginClassLoader;
  private ClassLoader bizClassLoader;

  public ClassLoaderForScript(ClassLoader pluginClassLoader, ClassLoader bizClassLoader) {
    this(pluginClassLoader, bizClassLoader, new URL[] {});
  }

  public ClassLoaderForScript(
      ClassLoader pluginClassLoader, ClassLoader bizClassLoader, URL[] urls) {
    super(urls);
    this.pluginClassLoader = pluginClassLoader;
    this.bizClassLoader = bizClassLoader;
  }

  @Override
  protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
    if (pluginClassLoader != null) {
      Class<?> aClass = null;
      try {
        aClass = pluginClassLoader.loadClass(name);
      } catch (ClassNotFoundException e) {
        if (bizClassLoader != null) {
          aClass = bizClassLoader.loadClass(name);
        }
      }
      if (aClass != null) {
        if (resolve) {
          resolveClass(aClass);
        }
        return aClass;
      }
    }
    return super.loadClass(name, resolve);
  }
}
