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

/** @author RinaisSuper */
public class CompiledScript {

  /** Script type, such as Java, Groovy */
  private String language;

  /** Script id */
  private String id;

  /** Script name for label */
  private String name;

  /** Script signature for checking the content modified or not */
  private String signature;

  /** Compiled object */
  private Object compiled;

  public CompiledScript(
      String id, String language, Object compiled, String name, String signature) {
    this.language = language;
    this.id = id;
    this.compiled = compiled;
    this.name = name;
    this.signature = signature;
  }

  public String getLanguage() {
    return language;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getSignature() {
    return signature;
  }

  public Object getCompiled() {
    return compiled;
  }
}
