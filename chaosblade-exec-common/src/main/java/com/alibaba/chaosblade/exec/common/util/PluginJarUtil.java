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

import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;

/** @author Changjun Xiao */
public class PluginJarUtil {

  public static final Pattern COMPILE = Pattern.compile("plugins/(.*).jar");

  public static List<String> findAgentJarFileNames(Pattern pattern) {
    URL agentJarUrl = getAgentJarUrl();
    return findJarFileNames(agentJarUrl, pattern);
  }

  public static List<String> findJarFileNames(URL agentJarUrl, Pattern pattern) {
    JarFile jarFile = null;
    try {
      jarFile = getAgentJarFile(agentJarUrl);

      List<String> names = new ArrayList();
      for (Enumeration<JarEntry> entries = jarFile.entries(); entries.hasMoreElements(); ) {
        JarEntry jarEntry = entries.nextElement();
        if (pattern.matcher(jarEntry.getName()).matches()) {
          names.add(jarEntry.getName());
        }
      }
      return names;
    } catch (Exception e) {
    } finally {
      if (jarFile != null) {
        try {
          jarFile.close();
        } catch (IOException localIOException2) {
        }
      }
    }
    return Collections.emptyList();
  }

  public static URL getAgentJarUrl() {
    return PluginJarUtil.class.getProtectionDomain().getCodeSource().getLocation();
  }

  private static JarFile getAgentJarFile(URL agentJarUrl) {
    if (agentJarUrl == null) {
      return null;
    }
    try {
      return new JarFile(getAgentJarFileName(agentJarUrl));
    } catch (IOException e) {
    }
    return null;
  }

  private static String getAgentJarFileName(URL agentJarUrl) {
    if (agentJarUrl == null) {
      return null;
    }
    try {
      return URLDecoder.decode(agentJarUrl.getFile().replace("+", "%2B"), "UTF-8");
    } catch (IOException e) {
    }
    return null;
  }

  /**
   * Get plugin jar
   *
   * @return
   */
  public static Map<String, URL> getPluginFiles(Class clazz) {
    List<String> agentJarFileNames = findAgentJarFileNames(COMPILE);
    Map<String, URL> urls = new HashMap<String, URL>(agentJarFileNames.size());
    for (String agentJarFileName : agentJarFileNames) {
      String name =
          agentJarFileName.substring(
              agentJarFileName.lastIndexOf('/') + 1, agentJarFileName.lastIndexOf('.'));
      URL url = clazz.getResource("/" + agentJarFileName);
      urls.put(name, url);
    }
    return urls;
  }
}
