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

import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.common.exception.InterruptProcessException;
import com.alibaba.chaosblade.exec.common.model.action.ActionExecutor;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherModel;
import com.alibaba.chaosblade.exec.common.plugin.MethodConstant;
import com.alibaba.chaosblade.exec.common.util.StringUtil;
import com.alibaba.chaosblade.exec.common.util.StringUtils;
import com.alibaba.chaosblade.exec.plugin.jvm.Base64Util;
import com.alibaba.chaosblade.exec.plugin.jvm.JvmConstant;
import com.alibaba.chaosblade.exec.plugin.jvm.StoppableActionExecutor;
import com.alibaba.chaosblade.exec.plugin.jvm.script.base.CompiledScript;
import com.alibaba.chaosblade.exec.plugin.jvm.script.base.DefaultScriptEngineService;
import com.alibaba.chaosblade.exec.plugin.jvm.script.base.ExecutableScript;
import com.alibaba.chaosblade.exec.plugin.jvm.script.base.Script;
import com.alibaba.chaosblade.exec.plugin.jvm.script.base.ScriptException;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** @author Changjun Xiao */
public class DynamicScriptExecutor implements ActionExecutor, StoppableActionExecutor {

  private static DefaultScriptEngineService engineService = new DefaultScriptEngineService();

  static {
    engineService.initialize();
  }

  /**
   * @param enhancerModel
   * @throws Exception
   */
  @Override
  public void run(EnhancerModel enhancerModel) throws Exception {
    // check experiment exists or not
    MatcherModel matcher = enhancerModel.getMatcherModel();
    String scriptId = createScriptId(matcher);
    if (scriptId == null) {
      throw new IllegalArgumentException("can not get script id from matcher model");
    }
    ScriptTypeEnum scriptType = getScriptType(enhancerModel);
    String scriptContent = getScriptContent(enhancerModel);
    String scriptName = getScriptName(scriptId, enhancerModel);
    // create script object
    Script script = new Script(scriptId, "", scriptName, scriptContent, scriptType.getName());
    ClassLoader classLoader = getClassLoader(scriptType, enhancerModel);
    // compile script content
    CompiledScript compiledScript = engineService.compile(classLoader, script, null);
    Map<String, Object> params = createParams(scriptType, enhancerModel);
    // execute script
    ExecutableScript executableScript = engineService.execute(compiledScript, params);
    Object result = null;
    try {
      result = executableScript.run();
    } catch (ScriptException e) {
      if (e.getCause() instanceof InvocationTargetException) {
        InvocationTargetException targetException = (InvocationTargetException) e.getCause();
        throw InterruptProcessException.throwThrowsImmediately(targetException.getCause());
      }
      throw e;
    } catch (Exception e) {
      throw e;
    }
    if (result != null) {
      // if result is not null, then return the value
      InterruptProcessException.throwReturnImmediately(result);
    }
    // change parameters if they are modified
    checkAndChangeParameters(scriptType, params, enhancerModel);
  }

  /**
   * Check and change the parameters
   *
   * @param scriptType
   * @param params
   * @param enhancerModel
   */
  private void checkAndChangeParameters(
      ScriptTypeEnum scriptType, Map<String, Object> params, EnhancerModel enhancerModel) {
    Object[] methodArguments = enhancerModel.getMethodArguments();
    if (scriptType == ScriptTypeEnum.GROOVY) {
      params = (Map<String, Object>) params.get(JvmConstant.GROOVY_VAL_KEY);
    }
    if (methodArguments != null) {
      for (int i = 0; i < methodArguments.length; i++) {
        methodArguments[i] = params.get(i + "");
      }
    }
  }

  private ClassLoader getClassLoader(ScriptTypeEnum scriptType, EnhancerModel enhancerModel)
      throws Exception {
    ClassLoader loader = enhancerModel.getClassLoader();
    if (scriptType == ScriptTypeEnum.GROOVY) {
      loader =
          new ClassLoaderForScript(
              this.getClass().getClassLoader(), enhancerModel.getClassLoader());
    }

    List<URL> urlList = new ArrayList<URL>();
    String externalJarFile = enhancerModel.getActionFlag(JvmConstant.FLAG_NAME_EXTERNAL_JAR);
    if (StringUtils.isNotBlank(externalJarFile)) {
      String[] jars = externalJarFile.split("\\;");
      for (String jarName : jars) {
        urlList.add(new URL(jarName));
      }
    }
    String externalJarFilePath =
        enhancerModel.getActionFlag(JvmConstant.FLAG_NAME_EXTERNAL_JAR_PATH);
    if (StringUtils.isNotBlank(externalJarFilePath)) {
      File file = new File(externalJarFilePath);
      String[] list =
          file.list(
              new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                  return name.endsWith(JvmConstant.JAR_FILE_SUFFIX);
                }
              });
      for (String jarName : list) {
        urlList.add(new URL(JvmConstant.FILE_PROTOCOL + externalJarFilePath + "/" + jarName));
      }
    }
    if (urlList.size() > 0) {
      URL[] urls = urlList.toArray(new URL[urlList.size()]);
      loader =
          new ClassLoaderForScript(
              this.getClass().getClassLoader(), enhancerModel.getClassLoader(), urls);
    }

    return loader;
  }

  private String getScriptName(String scriptId, EnhancerModel enhancerModel) {
    String scriptName = enhancerModel.getActionFlag(JvmConstant.FLAG_NAME_SCRIPT_NAME);
    if (StringUtil.isBlank(scriptName)) {
      scriptName = scriptId;
    }
    return scriptName;
  }

  /**
   * Get script content
   *
   * @param enhancerModel
   * @return
   */
  private String getScriptContent(EnhancerModel enhancerModel) {
    // get script file
    String scriptFile = enhancerModel.getActionFlag(JvmConstant.FLAG_NAME_SCRIPT_FILE);
    String scriptContent = enhancerModel.getActionFlag(JvmConstant.FLAG_NAME_SCRIPT_CONTENT);
    if (StringUtil.isBlank(scriptFile) && StringUtil.isBlank(scriptContent)) {
      throw new IllegalArgumentException("less script file or content");
    }
    if (!StringUtil.isBlank(scriptFile) && !StringUtil.isBlank(scriptContent)) {
      throw new IllegalArgumentException(
          "must only specify one flag between script file and script content");
    }
    // parse script file
    if (!StringUtil.isBlank(scriptFile)) {
      StringBuilder sb = new StringBuilder();
      BufferedReader reader = null;
      try {
        reader = new BufferedReader(new FileReader(scriptFile));
        String line;
        while ((line = reader.readLine()) != null) {
          sb.append(line).append('\n');
        }
        reader.close();
      } catch (IOException e) {

      } finally {
        if (reader != null) {
          try {
            reader.close();
          } catch (IOException e) {
          }
        }
      }
      return sb.toString();
    }
    String decodeScriptContent =
        enhancerModel.getActionFlag(JvmConstant.FLAG_NAME_SCRIPT_CONTENT_DECODE);
    if (StringUtil.isBlank(decodeScriptContent)) {
      // parse script content and cache
      scriptContent = scriptContent.replaceAll(" ", "+");
      String decode = Base64Util.decode(scriptContent.getBytes(Charset.forName("UTF-8")));
      enhancerModel.addActionFlag(JvmConstant.FLAG_NAME_SCRIPT_CONTENT_DECODE, decode);
      return decode;
    }
    return decodeScriptContent;
  }

  /**
   * Get script type
   *
   * @param enhancerModel
   * @return
   */
  private ScriptTypeEnum getScriptType(EnhancerModel enhancerModel) {
    // get script type
    String scriptType = enhancerModel.getActionFlag(JvmConstant.FLAG_NAME_SCRIPT_TYPE);
    if (StringUtil.isBlank(scriptType)) {
      return ScriptTypeEnum.JAVA;
    }
    if (scriptType.equalsIgnoreCase(JvmConstant.SCRIPT_TYPE_JAVA)) {
      return ScriptTypeEnum.JAVA;
    }
    if (scriptType.equalsIgnoreCase(JvmConstant.SCRIPT_TYPE_GROOVY)) {
      return ScriptTypeEnum.GROOVY;
    }
    throw new IllegalArgumentException(JvmConstant.FLAG_NAME_SCRIPT_TYPE + " value is illegal");
  }

  @Override
  public void stop(EnhancerModel enhancerModel) throws Exception {
    MatcherModel matcherModel = enhancerModel.getMatcherModel();
    String scriptId = createScriptId(matcherModel);
    if (scriptId != null) {
      engineService.cleanCompiledScript(scriptId);
    }
  }

  /**
   * Create script id
   *
   * @param matcherModel
   * @return
   */
  private String createScriptId(MatcherModel matcherModel) {
    if (matcherModel == null) {
      return null;
    }
    final String className = matcherModel.get(MethodConstant.CLASS_MATCHER_NAME);
    final String methodName = matcherModel.get(MethodConstant.METHOD_MATCHER_NAME);
    return className + "#" + methodName;
  }

  /**
   * Create parameters for script
   *
   * @param scriptType
   * @param enhancerModel
   * @return
   */
  private Map<String, Object> createParams(ScriptTypeEnum scriptType, EnhancerModel enhancerModel) {
    HashMap<String, Object> params = new HashMap<String, Object>();
    params.put(JvmConstant.SCRIPT_INVOKE_TARGET, enhancerModel.getObject());
    params.put(JvmConstant.SCRIPT_INVOKE_RETURN, enhancerModel.getReturnValue());
    Object[] methodArguments = enhancerModel.getMethodArguments();
    if (methodArguments != null) {
      for (int i = 0; i < methodArguments.length; i++) {
        params.put("" + i, methodArguments[i]);
      }
    }
    if (scriptType == ScriptTypeEnum.JAVA) {
      return params;
    }
    // for groovy
    HashMap<String, Object> result = new HashMap<String, Object>();
    result.put(JvmConstant.GROOVY_VAL_KEY, params);
    return result;
  }
}
