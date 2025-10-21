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
import com.alibaba.chaosblade.exec.common.model.action.delay.DelayActionSpec;
import com.alibaba.chaosblade.exec.common.model.action.exception.ThrowCustomExceptionActionSpec;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherSpec;
import java.util.List;

/** @author Changjun Xiao */
public abstract class FrameworkModelSpec extends BaseModelSpec {

  public FrameworkModelSpec() {
    addDelayActionSpec();
    addThrowExceptionActionSpec();

    addChildNewMatchersToAllActions();
  }

  private void addThrowExceptionActionSpec() {
    this.addActionSpec(new ThrowCustomExceptionActionSpec());
  }

  private void addDelayActionSpec() {
    DelayActionSpec delayActionSpec = new DelayActionSpec();
    this.addActionSpec(delayActionSpec);
  }

  private void addChildNewMatchersToAllActions() {
    List<MatcherSpec> newMatcherSpecs = createNewMatcherSpecs();
    if (newMatcherSpecs == null) {
      return;
    }
    for (MatcherSpec newMatcherSpec : newMatcherSpecs) {
      addMatcherDefToAllActions(newMatcherSpec);
    }
  }

  public void addAllMatchersToTheAction(ActionSpec actionSpec) {
    List<MatcherSpec> newMatcherSpecs = createNewMatcherSpecs();
    if (newMatcherSpecs == null) {
      return;
    }
    for (MatcherSpec matcherSpec : newMatcherSpecs) {
      actionSpec.addMatcherDesc(matcherSpec);
    }
  }

  /** */
  protected abstract List<MatcherSpec> createNewMatcherSpecs();

  @Override
  protected PredicateResult preMatcherPredicate(Model matcherSpecs) {
    return PredicateResult.success();
  }
}
