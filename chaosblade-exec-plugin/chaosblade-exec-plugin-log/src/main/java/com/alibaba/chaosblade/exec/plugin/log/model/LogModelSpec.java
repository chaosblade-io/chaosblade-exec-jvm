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

package com.alibaba.chaosblade.exec.plugin.log.model;

import com.alibaba.chaosblade.exec.common.aop.PredicateResult;
import com.alibaba.chaosblade.exec.common.model.FrameworkModelSpec;
import com.alibaba.chaosblade.exec.common.model.Model;
import com.alibaba.chaosblade.exec.common.model.action.ActionSpec;
import com.alibaba.chaosblade.exec.common.model.action.delay.DelayActionSpec;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherModel;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherSpec;
import com.alibaba.chaosblade.exec.plugin.log.LogConstant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/** @author shizhi.zhu@qunar.com */
public class LogModelSpec extends FrameworkModelSpec {

  public LogModelSpec() {
    addActionExample();
  }

  private void addActionExample() {
    List<ActionSpec> actions = getActions();
    for (ActionSpec action : actions) {
      if (action instanceof DelayActionSpec) {
        action.setLongDesc("Log framework delay experiment");
        action.setExample(
            "# Do a delay 1s experiment take effect 100 times\n"
                + "blade create log delay --logback --time 1000 --effect-count 100\n");
      }
    }
  }

  @Override
  public String getShortDesc() {
    return "log experiment";
  }

  @Override
  public String getLongDesc() {
    return "log experiment for testing service delay.";
  }

  @Override
  protected List<MatcherSpec> createNewMatcherSpecs() {
    ArrayList<MatcherSpec> matcherSpecs = new ArrayList<MatcherSpec>();
    matcherSpecs.add(new LogbackModelSpec());
    matcherSpecs.add(new Log4j2ModelSpec());
    return matcherSpecs;
  }

  @Override
  public String getTarget() {
    return LogConstant.TARGET;
  }

  @Override
  protected PredicateResult preMatcherPredicate(Model model) {
    if (model == null) {
      return PredicateResult.fail("matcher not found for log");
    }
    MatcherModel matcher = model.getMatcher();
    Set<String> keySet = matcher.getMatchers().keySet();
    for (String key : keySet) {
      if (key.equals(LogConstant.LOG4J2_KEY) || key.equals(LogConstant.LOGBACK_KEY)) {
        return PredicateResult.success();
      }
    }
    return PredicateResult.fail("less necessary matcher is log4j2 or logback for log");
  }
}
