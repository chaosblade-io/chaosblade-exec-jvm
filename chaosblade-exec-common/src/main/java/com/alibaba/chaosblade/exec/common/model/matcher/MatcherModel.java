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

package com.alibaba.chaosblade.exec.common.model.matcher;

import java.util.LinkedHashMap;
import java.util.Map;

/** @author Changjun Xiao */
public class MatcherModel {

  private Map<String, Object> matchers;

  public MatcherModel() {
    this.matchers = new LinkedHashMap<String, Object>();
  }

  public MatcherModel(Map<String, Object> matchers) {
    this.matchers = matchers;
  }

  public void add(String name, Object value) {
    this.matchers.put(name, value);
  }

  public <T> T get(String name) {
    return (T) matchers.get(name);
  }

  public Map<String, Object> getMatchers() {
    return matchers;
  }
}
