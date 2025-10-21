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

package com.alibaba.chaosblade.exec.common.model;

import com.alibaba.chaosblade.exec.common.model.action.ActionModel;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherModel;

/** @author Changjun Xiao */
public class Model {
  private String target;
  private MatcherModel matcher;
  private ActionModel action;

  public Model(String target, String actionName) {
    this.target = target;
    this.matcher = new MatcherModel();
    this.action = new ActionModel(actionName);
  }

  public String getTarget() {
    return target;
  }

  public void setTarget(String target) {
    this.target = target;
  }

  public String getActionName() {
    return action.getName();
  }

  public ActionModel getAction() {
    return action;
  }

  public void setAction(ActionModel action) {
    this.action = action;
  }

  public MatcherModel getMatcher() {
    return matcher;
  }

  public void setMatcher(MatcherModel matcher) {
    this.matcher = matcher;
  }

  @Override
  public String toString() {
    return "Model{"
        + "target='"
        + target
        + '\''
        + ", matchers="
        + matcher.getMatchers().toString()
        + ", action="
        + action.getName()
        + ", flags="
        + action.getFlags().toString()
        + '}';
  }
}
