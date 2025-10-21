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
import com.alibaba.chaosblade.exec.common.model.action.DirectlyInjectionAction;
import com.alibaba.chaosblade.exec.common.model.matcher.EffectCountMatcherSpec;
import com.alibaba.chaosblade.exec.common.model.matcher.EffectPercentMatcherSpec;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherSpec;
import com.alibaba.chaosblade.exec.common.model.prepare.AgentPrepareSpec;
import com.alibaba.chaosblade.exec.common.model.prepare.PrepareSpec;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** @author Changjun Xiao */
public abstract class BaseModelSpec implements ModelSpec {

  private static final Logger LOGGER = LoggerFactory.getLogger(BaseModelSpec.class);

  private ConcurrentHashMap<String, ActionSpec> actionSpecs =
      new ConcurrentHashMap<String, ActionSpec>();

  @Override
  public List<ActionSpec> getActions() {
    return new ArrayList<ActionSpec>(actionSpecs.values());
  }

  @Override
  public Map<String, ActionSpec> getActionSpecs() {
    return actionSpecs;
  }

  @Override
  public ActionSpec getActionSpec(String actionName) {
    return actionSpecs.get(actionName);
  }

  @Override
  public void addActionSpec(ActionSpec actionSpec) {
    ActionSpec oldActionSpec = actionSpecs.putIfAbsent(actionSpec.getName(), actionSpec);
    if (oldActionSpec != null) {
      LOGGER.warn("{} action has defined in {} target model", actionSpec.getName(), getTarget());
      return;
    }
    if (!(actionSpec instanceof DirectlyInjectionAction)) {
      // add effect matcher
      actionSpec.addMatcherDesc(new EffectCountMatcherSpec());
      actionSpec.addMatcherDesc(new EffectPercentMatcherSpec());
    }
  }

  @Override
  public PredicateResult predicate(Model model) {
    // check action
    Collection<ActionSpec> actionSpecs = getActionSpecs().values();
    if (actionSpecs == null) {
      LOGGER.error("the model action desc is null. target: {}", this.getTarget());
      return PredicateResult.fail("model action desc is null");
    }
    for (ActionSpec actionSpec : actionSpecs) {
      if (!actionSpec.getName().equals(model.getActionName())) {
        continue;
      }
      PredicateResult result = actionSpec.predicate(model.getAction());
      if (!result.isSuccess()) {
        LOGGER.error(
            "the model action predicate failed. target: {}, action: {}",
            this.getTarget(),
            actionSpec.getName());
        return PredicateResult.fail(result.getErr());
      }

      // check matcher
      result = preMatcherPredicate(model);
      if (!result.isSuccess()) {
        return PredicateResult.fail(result.getErr());
      }
      Collection<MatcherSpec> matcherSpecs = actionSpec.getMatcherSpecs().values();
      if (matcherSpecs == null) {
        continue;
      }
      for (MatcherSpec matcherSpec : matcherSpecs) {
        result = matcherSpec.predicate(model.getMatcher());
        if (!result.isSuccess()) {
          return PredicateResult.fail(result.getErr());
        }
      }
    }
    return PredicateResult.success();
  }

  @Override
  public void addMatcherDefToAllActions(MatcherSpec matcherSpec) {
    for (ActionSpec actionSpec : actionSpecs.values()) {
      actionSpec.addMatcherDesc(matcherSpec);
    }
  }

  /**
   * @param matcherSpecs
   * @return
   */
  protected abstract PredicateResult preMatcherPredicate(Model matcherSpecs);

  @Override
  public PrepareSpec getPrepareSpec() {
    return new AgentPrepareSpec();
  }

  @Override
  public String getScope() {
    return "host";
  }
}
