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

package com.alibaba.chaosblade.exec.plugin.jvm.script.base.finder;

import com.alibaba.chaosblade.exec.plugin.jvm.script.base.Script;
import com.alibaba.chaosblade.exec.plugin.jvm.script.base.ScriptFinder;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** @author RinaisSuper */
public class CompoundScriptFinder implements ScriptFinder {

  private static final Logger LOGGER = LoggerFactory.getLogger(CompoundScriptFinder.class);

  private List<ScriptFinder> scriptFinderList = new ArrayList<ScriptFinder>();

  public CompoundScriptFinder add(ScriptFinder scriptFinder) {
    if (scriptFinder == null) {
      throw new IllegalArgumentException("scriptFinder not null");
    }
    scriptFinderList.add(scriptFinder);
    return this;
  }

  @Override
  public Script find(String scriptId) {
    for (ScriptFinder scriptFinder : scriptFinderList) {
      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug("find script by finder:" + scriptFinder.getClass().getCanonicalName());
      }
      try {
        Script script = scriptFinder.find(scriptId);
        if (script != null) {
          return script;
        }
      } catch (Exception exception) {
        if (scriptFinderList.indexOf(scriptFinder) == scriptFinderList.size() - 1) {
          LOGGER.error(exception.getMessage(), exception);
        } else {
          LOGGER.info("Try to continue with next finder");
        }
      }
    }
    return null;
  }
}
