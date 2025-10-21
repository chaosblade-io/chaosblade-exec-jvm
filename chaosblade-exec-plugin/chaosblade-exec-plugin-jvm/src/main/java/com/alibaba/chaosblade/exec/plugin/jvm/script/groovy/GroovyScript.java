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

package com.alibaba.chaosblade.exec.plugin.jvm.script.groovy;

import com.alibaba.chaosblade.exec.plugin.jvm.script.base.CompiledScript;
import com.alibaba.chaosblade.exec.plugin.jvm.script.base.ExecutableScript;
import com.alibaba.chaosblade.exec.plugin.jvm.script.base.ScriptException;
import groovy.lang.Script;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Collections;
import org.codehaus.groovy.GroovyBugError;

/** @author RinaisSuper */
public class GroovyScript implements ExecutableScript {

  private final CompiledScript compiledScript;

  private final Script script;

  public GroovyScript(CompiledScript compiledScript, Script script) {
    this.compiledScript = compiledScript;
    this.script = script;
  }

  @Override
  public Object run() {
    try {
      return AccessController.doPrivileged(
          new PrivilegedAction<Object>() {
            @Override
            public Object run() {
              return script.run();
            }
          });
    } catch (AssertionError assertionError) {
      if (assertionError instanceof GroovyBugError) {
        final String message =
            "encountered bug in Groovy while executing script [" + compiledScript.getId() + "]";
        throw new ScriptException(
            message,
            assertionError,
            Collections.<String>emptyList(),
            compiledScript.toString(),
            compiledScript.getLanguage());
      }
      final StackTraceElement[] elements = assertionError.getStackTrace();
      if (elements.length > 0
          && "org.codehaus.groovy.runtime.InvokerHelper".equals(elements[0].getClassName())) {
        throw new ScriptException(
            "error evaluating " + compiledScript.getId(),
            assertionError,
            Collections.<String>emptyList(),
            "",
            compiledScript.getLanguage());
      }
      throw assertionError;
    } catch (Exception e) {
      throw new ScriptException(
          "error evaluating " + compiledScript.getId(),
          e,
          Collections.<String>emptyList(),
          "",
          compiledScript.getLanguage());
    }
  }
}
