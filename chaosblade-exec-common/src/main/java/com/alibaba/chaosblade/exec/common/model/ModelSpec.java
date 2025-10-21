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

import com.alibaba.chaosblade.exec.common.aop.PredicateResult;
import com.alibaba.chaosblade.exec.common.model.action.ActionSpec;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherSpec;
import com.alibaba.chaosblade.exec.common.model.prepare.PrepareSpec;
import java.util.List;
import java.util.Map;

/** @author Changjun Xiao */
public interface ModelSpec {

  /**
   * Get the experiment model target name
   *
   * @return
   */
  String getTarget();

  /**
   * Get the short description
   *
   * @return
   */
  String getShortDesc();

  /**
   * Get the long description
   *
   * @return
   */
  String getLongDesc();

  /**
   * Get the experiment actions
   *
   * @return
   */
  List<ActionSpec> getActions();

  /**
   * Get the experiment actions with action name
   *
   * @return
   */
  Map<String, ActionSpec> getActionSpecs();

  /**
   * Get action spec by action name
   *
   * @param actionName
   * @return
   */
  ActionSpec getActionSpec(String actionName);

  /**
   * Add action spec
   *
   * @param actionSpec
   */
  void addActionSpec(ActionSpec actionSpec);

  /**
   * Predicate the experiment model
   *
   * @param model
   * @return
   */
  PredicateResult predicate(Model model);

  /**
   * Add matcher spec to all actions of the experiment model
   *
   * @param matcherSpec
   */
  void addMatcherDefToAllActions(MatcherSpec matcherSpec);

  /**
   * Get experiment prepare, default is {@link
   * com.alibaba.chaosblade.exec.common.model.prepare.AgentPrepareSpec}
   *
   * @return
   */
  PrepareSpec getPrepareSpec();

  /**
   * Get execute scope
   *
   * @return
   */
  String getScope();
}
