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

import com.alibaba.chaosblade.exec.common.util.ObjectsUtil;
import java.util.Collections;
import java.util.List;

/** @author RinaisSuper */
public class ScriptException extends RuntimeException {

  private List<String> scriptStack;
  private String scriptId;
  private String lang;

  public ScriptException(
      String message, Throwable cause, List<String> scriptStack, String scriptId, String lang) {
    super(message, cause);
    this.scriptStack = Collections.unmodifiableList(ObjectsUtil.requireNonNull(scriptStack));
    this.scriptId = ObjectsUtil.requireNonNull(scriptId);
    this.lang = ObjectsUtil.requireNonNull(lang);
  }

  public ScriptException(String message) {
    super(message);
  }

  public ScriptException(String message, Throwable cause) {
    super(message, cause);
  }
}
