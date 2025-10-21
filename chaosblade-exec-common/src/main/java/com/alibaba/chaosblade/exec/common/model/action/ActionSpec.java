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

import com.alibaba.chaosblade.exec.common.aop.PredicateResult;
import com.alibaba.chaosblade.exec.common.model.FlagSpec;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherSpec;
import java.util.List;
import java.util.Map;

/** @author Changjun Xiao */
public interface ActionSpec {

  /**
   * Get action name
   *
   * @return
   */
  String getName();

  /**
   * Get action aliases
   *
   * @return
   */
  String[] getAliases();

  /**
   * Get short description
   *
   * @return
   */
  String getShortDesc();

  /**
   * Get long description
   *
   * @return
   */
  String getLongDesc();

  /**
   * Get long description
   *
   * @return
   */
  void setLongDesc(String longDesc);

  /**
   * Get experiment matcher specification
   *
   * @return
   */
  List<MatcherSpec> getMatchers();

  /**
   * Get action flags
   *
   * @return
   */
  List<FlagSpec> getActionFlags();

  /**
   * Predicate the model arguments
   *
   * @param actionModel @return
   */
  PredicateResult predicate(ActionModel actionModel);

  /**
   * Get the matcher specs
   *
   * @return
   */
  Map<String, MatcherSpec> getMatcherSpecs();

  /**
   * Add matcher spec
   *
   * @param matcherSpec
   */
  void addMatcherDesc(MatcherSpec matcherSpec);

  /**
   * Get the action executor
   *
   * @return
   */
  ActionExecutor getActionExecutor();

  /**
   * Get the experiment example
   *
   * @return
   */
  String getExample();

  /**
   * Set the experiment example
   *
   * @param example
   * @return
   */
  void setExample(String example);

  /**
   * Get the scenario categories
   *
   * @return
   */
  String[] getCategories();
}
