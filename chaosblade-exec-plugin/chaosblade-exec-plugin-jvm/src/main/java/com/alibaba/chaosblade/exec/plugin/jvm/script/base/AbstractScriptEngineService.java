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
import com.alibaba.chaosblade.exec.plugin.jvm.script.base.cache.LruScriptCache;
import com.alibaba.chaosblade.exec.plugin.jvm.script.base.cache.ScriptCache;
import com.alibaba.chaosblade.exec.plugin.jvm.script.base.finder.ScriptEngineFinder;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/** @author RinaisSuper */
public abstract class AbstractScriptEngineService implements ScriptEngineService {

  private ScriptMetrics scriptMetrics;

  private ScriptEngineFinder scriptEngineFinder;

  private ScriptCache<String, CompiledScript> scriptCache;

  private int CACHE_SIZE = 100;

  private AtomicBoolean initialized = new AtomicBoolean(false);

  public AbstractScriptEngineService() {}

  public void setScriptMetrics(ScriptMetrics scriptMetrics) {
    this.scriptMetrics = scriptMetrics;
  }

  public void setScriptEngineFinder(ScriptEngineFinder scriptEngineFinder) {
    this.scriptEngineFinder = scriptEngineFinder;
  }

  public void setScriptCache(ScriptCache<String, CompiledScript> scriptCache) {
    this.scriptCache = scriptCache;
  }

  @Override
  public CompiledScript compile(
      ClassLoader classLoader, Script script, Map<String, String> config) {
    checkInitialized();
    ObjectsUtil.requireNonNull(script);
    ObjectsUtil.requireNonNull(script.getId());
    ObjectsUtil.requireNonNull(script.getContent());
    ObjectsUtil.requireNonNull(script.getSignature());
    ObjectsUtil.requireNonNull(script.getLanguage());
    CompiledScript compiledScript = scriptCache.get(script.getId());
    if (compiledScript == null) {
      return doCompile(classLoader, script, config);
    } else {
      String oldSignature = compiledScript.getSignature();
      if (oldSignature.equals(script.getSignature())) {
        return compiledScript;
      }
      return doCompile(classLoader, script, config);
    }
  }

  protected CompiledScript doCompile(
      ClassLoader classLoader, Script script, Map<String, String> options) {
    String scriptId = script.getId();
    ScriptEngine scriptEngine = getScriptEngineForLang(script.getLanguage());
    Object compiledObject = scriptEngine.compile(script, classLoader, options);
    CompiledScript compiledScript =
        new CompiledScript(
            scriptId,
            script.getLanguage(),
            compiledObject,
            script.getName(),
            script.getSignature());
    scriptCache.put(scriptId, compiledScript);
    scriptMetrics.incrCompiledScript();
    return compiledScript;
  }

  @Override
  public ExecutableScript execute(CompiledScript compiledScript, Map<String, Object> params) {
    checkInitialized();
    return getScriptEngineForLang(compiledScript.getLanguage()).execute(compiledScript, params);
  }

  private ScriptEngine getScriptEngineForLang(String lang) {
    ScriptEngine scriptEngine = scriptEngineFinder.findByLang(lang);
    if (scriptEngine == null) {
      throw new IllegalArgumentException("Script language not supported [" + lang + "]");
    }
    return scriptEngine;
  }

  @Override
  public void initialize() {
    if (initialized.compareAndSet(false, true)) {
      initScriptMetrics();
      initScriptEngineFinder();
      initScriptCache();
    }
  }

  private void initScriptCache() {
    if (this.scriptCache == null) {
      this.scriptCache = new LruScriptCache<String, CompiledScript>(CACHE_SIZE);
    }
  }

  private void initScriptEngineFinder() {
    if (scriptEngineFinder == null) {
      this.scriptEngineFinder = new ServiceProviderScriptEngineFinder(this);
    }
  }

  private void initScriptMetrics() {
    if (scriptMetrics == null) {
      scriptMetrics = new DefaultScriptMetrics();
    }
  }

  @Override
  public boolean cleanCompiledScript(String scriptId) {
    return this.scriptCache.evict(scriptId);
  }

  @Override
  public void cleanAllCompiledScripts() {
    this.scriptCache.clean();
  }

  private void checkInitialized() {
    if (!initialized.get()) {
      throw new IllegalStateException("Script engine service must initialize first");
    }
  }
}
