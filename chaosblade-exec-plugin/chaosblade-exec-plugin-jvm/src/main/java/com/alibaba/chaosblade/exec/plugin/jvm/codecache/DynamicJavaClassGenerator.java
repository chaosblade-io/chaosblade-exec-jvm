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

package com.alibaba.chaosblade.exec.plugin.jvm.codecache;

import com.alibaba.chaosblade.exec.plugin.jvm.script.base.CompiledScript;
import com.alibaba.chaosblade.exec.plugin.jvm.script.base.DefaultScriptEngineService;
import com.alibaba.chaosblade.exec.plugin.jvm.script.base.Script;
import com.alibaba.chaosblade.exec.plugin.jvm.script.model.ScriptTypeEnum;
import java.util.UUID;

/**
 * @author Jinglei Li
 * @date 2019-07-31
 * @mail liwx2000@gmail.com
 */
public class DynamicJavaClassGenerator {

  private static final int METHOD_COUNT_IN_CLASS = 16000;

  private static final String CLASS_NAME_PREFIX = "CodeCacheFillingObject";

  private static final DefaultScriptEngineService engineService = new DefaultScriptEngineService();

  static {
    engineService.initialize();
  }

  public Object generateObject() throws IllegalAccessException, InstantiationException {
    String suffix = UUID.randomUUID().toString().replaceAll("-", "");
    String className = CLASS_NAME_PREFIX + suffix;
    String content = generateJavaClassSourceCode(className);

    Script script = new Script(className, "", className, content, ScriptTypeEnum.JAVA.getName());
    CompiledScript compiledScript =
        engineService.compile(Thread.currentThread().getContextClassLoader(), script, null);

    Object compiled = compiledScript.getCompiled();
    if (null != compiled) {
      return ((Class<?>) compiled).newInstance();
    }

    return null;
  }

  private String generateJavaClassSourceCode(String className) {
    return "public class " + className + "{\n" + generateMethods() + "}";
  }

  private String generateMethods() {
    StringBuilder rootMethod = new StringBuilder("public void method() {\n");
    StringBuilder methods = new StringBuilder();

    for (int i = 0; i < METHOD_COUNT_IN_CLASS; i++) {
      String methodName = "method" + i;

      methods.append("public void ").append(methodName).append("() {}\n");

      rootMethod.append(methodName).append("();\n");
    }

    rootMethod.append("}\n");

    return methods.append(rootMethod).toString();
  }
}
