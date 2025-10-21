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

package com.alibaba.chaosblade.exec.service.build;

import com.alibaba.chaosblade.exec.common.aop.Plugin;
import com.alibaba.chaosblade.exec.common.model.ModelSpec;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import org.yaml.snakeyaml.DumperOptions.FlowStyle;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Tag;

/** @author Changjun Xiao */
public class BuildUtil {

  public static String generatePluginSpec(String pluginPath, String specFileName) {
    PluginSpecBean pluginSpecBean = new PluginSpecBean();
    pluginSpecBean.setVersion("v1");
    pluginSpecBean.setKind("plugin");
    List<ModelSpecBean> specs = new ArrayList<ModelSpecBean>();
    pluginSpecBean.setItems(specs);
    try {
      ClassLoader urlClassLoader = createClassLoader(pluginPath);
      File file = getFile(pluginPath, specFileName);
      ServiceLoader<Plugin> plugins = ServiceLoader.load(Plugin.class, urlClassLoader);
      Map<String, ModelSpec> modelSpecs = new HashMap<String, ModelSpec>();
      for (Plugin plugin : plugins) {
        ModelSpec modelSpec = plugin.getModelSpec();
        ModelSpec oldSpec = modelSpecs.get(modelSpec.getTarget());
        if (oldSpec != null) {
          continue;
        }
        modelSpecs.put(modelSpec.getTarget(), modelSpec);
        specs.add(new ModelSpecBean(plugin.getModelSpec()));
      }
      String dump = new Yaml().dumpAs(pluginSpecBean, Tag.MAP, FlowStyle.BLOCK);
      write(dump, file);
      return file.getPath();
    } catch (IOException e) {
      System.err.println("Warning: Failed to generate plugin spec: " + e.getMessage());
      // 在CI环境中，如果没有插件目录，我们仍然创建一个空的spec文件
      try {
        File file = getFile(pluginPath, specFileName);
        String dump = new Yaml().dumpAs(pluginSpecBean, Tag.MAP, FlowStyle.BLOCK);
        write(dump, file);
        return file.getPath();
      } catch (IOException ex) {
        System.err.println("Error: Failed to create empty spec file: " + ex.getMessage());
      }
    }
    return null;
  }

  private static ClassLoader createClassLoader(String pluginPath) throws IOException {
    File file = new File(pluginPath);
    if (!file.exists() || !file.isDirectory()) {
      // 如果目录不存在，返回当前线程的类加载器
      return Thread.currentThread().getContextClassLoader();
    }

    String[] jarFiles =
        file.list(
            new FilenameFilter() {
              @Override
              public boolean accept(File dir, String name) {
                return name.endsWith(".jar");
              }
            });

    if (jarFiles == null || jarFiles.length == 0) {
      // 如果没有jar文件，返回当前线程的类加载器
      return Thread.currentThread().getContextClassLoader();
    }

    List<URL> urls = new ArrayList<URL>();
    for (String f : jarFiles) {
      urls.add(new URL("file:" + new File(pluginPath, f).getPath()));
    }
    URL[] u = new URL[urls.size()];
    return new URLClassLoader(urls.toArray(u));
  }

  private static File getFile(String path, String fileName) throws IOException {
    File dir = new File(path);
    if (!dir.exists()) {
      dir.mkdirs();
    }
    File file = new File(path, fileName);
    System.out.println(file.getPath());
    if (!file.exists()) {
      file.createNewFile();
    }
    return file;
  }

  private static void write(String content, File file) throws IOException {
    BufferedWriter writer = null;
    try {
      writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
      writer.write(content);
      writer.flush();
    } finally {
      if (writer != null) {
        try {
          writer.close();
        } catch (IOException e) {
        }
      }
    }
  }
}
