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

import com.alibaba.chaosblade.exec.plugin.jvm.script.base.finder.ScriptEngineFinder;
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Load script engine by spi
 *
 * @author RinaisSuper
 */
public class ServiceProviderScriptEngineFinder implements ScriptEngineFinder {

  private final Map<String, ScriptEngine> languageToScriptEngine;

  private final Map<Class<? extends ScriptEngine>, String> scriptEngineToLanguage;

  protected ScriptEngineService scriptEngineService;

  public ServiceProviderScriptEngineFinder(ScriptEngineService scriptEngineService) {
    this.scriptEngineService = scriptEngineService;
    languageToScriptEngine = new ConcurrentHashMap<String, ScriptEngine>();
    scriptEngineToLanguage = new ConcurrentHashMap<Class<? extends ScriptEngine>, String>();
    loadScriptEngines();
  }

  public void loadScriptEngines() {
    ServiceLoader<ScriptEngine> scriptEngineServiceLoader =
        ServiceLoader.load(
            ScriptEngine.class, ServiceProviderScriptEngineFinder.class.getClassLoader());
    Iterator<ScriptEngine> scriptEngineIterator = scriptEngineServiceLoader.iterator();
    while (scriptEngineIterator.hasNext()) {
      ScriptEngine scriptEngine = scriptEngineIterator.next();
      if (scriptEngineToLanguage.containsKey(scriptEngine.getClass())) {
        throw new IllegalArgumentException(
            "Script engine ["
                + scriptEngine.getClass()
                + "] already registered for language ["
                + scriptEngine.getLanguage()
                + "]");
      }
      scriptEngineToLanguage.put(scriptEngine.getClass(), scriptEngine.getLanguage());
      if (languageToScriptEngine.containsKey(scriptEngine.getLanguage())) {
        throw new IllegalArgumentException(
            "Script Language ["
                + scriptEngine.getLanguage()
                + "] already registered for engine ["
                + scriptEngine.getClass().getCanonicalName()
                + "]");
      }
      languageToScriptEngine.put(scriptEngine.getLanguage().toUpperCase(), scriptEngine);
    }
  }

  @Override
  public ScriptEngine findByLang(String language) {
    if (language == null) {
      return null;
    }
    return languageToScriptEngine.get(language.toUpperCase());
  }
}
