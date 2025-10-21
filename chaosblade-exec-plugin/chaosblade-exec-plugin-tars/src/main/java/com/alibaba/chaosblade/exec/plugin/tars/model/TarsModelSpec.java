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

package com.alibaba.chaosblade.exec.plugin.tars.model;

import com.alibaba.chaosblade.exec.common.aop.PredicateResult;
import com.alibaba.chaosblade.exec.common.model.FrameworkModelSpec;
import com.alibaba.chaosblade.exec.common.model.Model;
import com.alibaba.chaosblade.exec.common.model.action.ActionSpec;
import com.alibaba.chaosblade.exec.common.model.action.delay.DelayActionSpec;
import com.alibaba.chaosblade.exec.common.model.action.exception.ThrowCustomExceptionActionSpec;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherModel;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherSpec;
import com.alibaba.chaosblade.exec.plugin.tars.TarsConstant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author saikei
 * @email lishiji@huya.com
 */
public class TarsModelSpec extends FrameworkModelSpec {

  public TarsModelSpec() {
    addActionExample();
  }

  private void addActionExample() {
    List<ActionSpec> actions = getActions();
    for (ActionSpec action : actions) {
      if (action instanceof DelayActionSpec) {
        action.setLongDesc("Tars delay experiment");
        action.setExample(
            "# Do a delay 3s experiment on tars interface\n"
                + "blade create tars delay --time 3000 --client --servantname app.server.obj --functionname hello");
      } else if (action instanceof ThrowCustomExceptionActionSpec) {
        action.setLongDesc("Tars throws custom exception experiment");
        action.setExample(
            "# Do a throw custom exception experiment on tars interface\n"
                + "blade c tars throwCustomException --exception org.springframework.beans.BeansException --exception-message mock-beans-exception --client --servantname=app.server.obj");
      }
    }
  }

  @Override
  protected List<MatcherSpec> createNewMatcherSpecs() {
    ArrayList<MatcherSpec> matcherSpecs = new ArrayList<MatcherSpec>();
    matcherSpecs.add(new ClientMatcherSpec());
    matcherSpecs.add(new ServantMatcherSpec());
    matcherSpecs.add(new ServantNameMatcherSpec());
    matcherSpecs.add(new FunctionNameMatcherSpec());
    return matcherSpecs;
  }

  @Override
  public String getTarget() {
    return TarsConstant.TARGET_NAME;
  }

  @Override
  public String getShortDesc() {
    return "tars experiment";
  }

  @Override
  public String getLongDesc() {
    return "Tars experiment for testing service delay and exception.";
  }

  @Override
  protected PredicateResult preMatcherPredicate(Model model) {
    if (model == null) {
      return PredicateResult.fail("matcher not found for tars");
    }
    MatcherModel matcher = model.getMatcher();
    Set<String> keySet = matcher.getMatchers().keySet();
    for (String key : keySet) {
      if (key.equals(TarsConstant.CLIENT) || key.equals(TarsConstant.SERVANT)) {
        return PredicateResult.success();
      }
    }
    return PredicateResult.fail("less necessary matcher is client or servant for tars");
  }
}
