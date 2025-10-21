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
public class ScriptContext {

  private Map<String, String> options;

  public Map<String, String> getOptions() {
    return options;
  }

  public void setOptions(Map<String, String> options) {
    this.options = options;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ScriptContext)) {
      return false;
    }

    ScriptContext that = (ScriptContext) o;

    return options != null ? options.equals(that.options) : that.options == null;
  }

  @Override
  public int hashCode() {
    return options != null ? options.hashCode() : 0;
  }

  @Override
  public String toString() {
    return "ScriptContext{" + "options=" + options + '}';
  }
}
