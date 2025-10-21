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

package com.alibaba.chaosblade.exec.plugin.jvm.script.java;

import com.alibaba.chaosblade.exec.plugin.jvm.script.base.ExecutableScript;
import com.alibaba.chaosblade.exec.plugin.jvm.script.base.ScriptException;
import java.lang.reflect.Method;
import java.util.Map;

/** @author RinaisSuper */
public class JavaExecutableScript implements ExecutableScript {

  public static String RUN_METHOD = "run";

  private Object instance;

  private Map<String, Object> params;

  public JavaExecutableScript(Object instance, Map<String, Object> params) {
    this.instance = instance;
    this.params = params;
  }

  @Override
  public Object run() {
    try {
      Method method = instance.getClass().getDeclaredMethod(RUN_METHOD, Map.class);
      method.setAccessible(true);
      return method.invoke(this.instance, params);
    } catch (NoSuchMethodException nc) {
      throw new ScriptException(
          "No suitable method found,method name:"
              + RUN_METHOD
              + ",param Type:"
              + Map.class.getName(),
          nc);

    } catch (Exception ex) {
      throw new ScriptException("Execute method failed", ex);
    }
  }
}
