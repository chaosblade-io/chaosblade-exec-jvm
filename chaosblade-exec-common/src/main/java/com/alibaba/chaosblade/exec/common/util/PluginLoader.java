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

package com.alibaba.chaosblade.exec.common.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ServiceLoader;

/** @author Changjun Xiao */
public class PluginLoader {

  /**
   * Get embed jar
   *
   * @param serviceType service interface type
   * @param urlMap
   * @param <T>
   * @return
   * @throws IOException
   */
  public static <T> List<T> load(Class<T> serviceType, Map<String, URL> urlMap) throws IOException {
    URL[] urls = new URL[urlMap.size()];
    int idx = 0;
    Iterator<Entry<String, URL>> iterator = urlMap.entrySet().iterator();
    while (iterator.hasNext()) {
      Entry<String, URL> entry = iterator.next();
      File tempFile = createTempFile(entry.getKey(), entry.getValue());
      urls[idx++] = tempFile.toURI().toURL();
    }
    URLClassLoader classLoader = createPluginClassLoader(urls, serviceType.getClassLoader());
    return load(serviceType, classLoader);
  }

  private static URLClassLoader createPluginClassLoader(
      final URL[] urls, final ClassLoader parent) {
    if (System.getSecurityManager() != null) {
      return AccessController.doPrivileged(
          new PrivilegedAction<URLClassLoader>() {
            @Override
            public URLClassLoader run() {
              return new URLClassLoader(urls, parent);
            }
          });
    } else {
      return new URLClassLoader(urls, parent);
    }
  }

  public static <T> List<T> load(Class<T> serviceType, ClassLoader classLoader) {
    ServiceLoader<T> serviceLoader = ServiceLoader.load(serviceType, classLoader);
    List<T> plugins = new ArrayList<T>();
    for (T plugin : serviceLoader) {
      plugins.add(serviceType.cast(plugin));
    }
    return plugins;
  }

  public static File createTempFile(String jarName, URL url) throws IOException {
    InputStream jarStream = url.openStream();
    if (jarStream == null) {
      throw new FileNotFoundException(jarName + ".jar");
    }
    File file = File.createTempFile(jarName, ".jar", null);
    file.deleteOnExit();

    OutputStream out = new FileOutputStream(file);
    try {
      copy(jarStream, out, 8096, true);
      return file;
    } finally {
      out.close();
    }
  }

  private static int copy(
      InputStream input, OutputStream output, int bufferSize, boolean closeStreams)
      throws IOException {
    try {
      byte[] buffer = new byte[bufferSize];
      int count = 0;
      int n = 0;
      while (-1 != (n = input.read(buffer))) {
        output.write(buffer, 0, n);
        count += n;
      }
      return count;
    } finally {
      if (closeStreams) {
        input.close();
        output.close();
      }
    }
  }
}
