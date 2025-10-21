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

package com.alibaba.chaosblade.exec.common.model.action;

import com.alibaba.chaosblade.exec.common.model.matcher.MatcherSpec;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/** @author Changjun Xiao */
public abstract class BaseActionSpec implements ActionSpec {

  private ConcurrentHashMap<String, MatcherSpec> matcherSpecs =
      new ConcurrentHashMap<String, MatcherSpec>();

  private ActionExecutor actionExecutor;

  private String LongDesc;

  private String example;

  public BaseActionSpec(ActionExecutor actionExecutor) {
    this.actionExecutor = actionExecutor;
  }

  @Override
  public List<MatcherSpec> getMatchers() {
    return new ArrayList<MatcherSpec>(matcherSpecs.values());
  }

  @Override
  public Map<String, MatcherSpec> getMatcherSpecs() {
    return matcherSpecs;
  }

  @Override
  public void addMatcherDesc(MatcherSpec matcherSpec) {
    matcherSpecs.putIfAbsent(matcherSpec.getName(), matcherSpec);
  }

  @Override
  public ActionExecutor getActionExecutor() {
    return actionExecutor;
  }

  @Override
  public void setExample(String example) {
    this.example = example;
  }

  @Override
  public String getExample() {
    return example;
  }

  @Override
  public String getLongDesc() {
    return LongDesc;
  }

  @Override
  public void setLongDesc(String longDesc) {
    LongDesc = longDesc;
  }
}
