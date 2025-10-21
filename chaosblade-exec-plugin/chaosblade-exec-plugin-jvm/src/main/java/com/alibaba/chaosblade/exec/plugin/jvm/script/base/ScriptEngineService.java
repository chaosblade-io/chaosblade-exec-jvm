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

package com.alibaba.chaosblade.exec.plugin.jvm.script.base;

import java.util.Map;

/** @author RinaisSuper */
public interface ScriptEngineService {

  /** Initialization operation. Must invoke it before compile script. */
  void initialize();

  /**
   * Compile script
   *
   * @param classLoader
   * @param script
   * @param configs for compile, can be null
   * @return
   */
  CompiledScript compile(ClassLoader classLoader, Script script, Map<String, String> configs);

  /**
   * Execute script
   *
   * @param compiledScript
   * @param params
   * @return
   */
  ExecutableScript execute(CompiledScript compiledScript, Map<String, Object> params);

  /**
   * Clean compiled script by script id
   *
   * @param scriptId
   * @return true if clean success
   */
  boolean cleanCompiledScript(String scriptId);

  /** Clean all compiled scripts */
  void cleanAllCompiledScripts();
}
