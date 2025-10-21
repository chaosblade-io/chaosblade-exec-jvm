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

package com.alibaba.chaosblade.exec.plugin.security;

import com.alibaba.chaosblade.exec.common.model.FrameworkModelSpec;
import com.alibaba.chaosblade.exec.common.model.action.ActionSpec;
import com.alibaba.chaosblade.exec.common.model.action.delay.DelayActionSpec;
import com.alibaba.chaosblade.exec.common.model.action.exception.ThrowCustomExceptionActionSpec;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherSpec;
import java.util.ArrayList;
import java.util.List;

/** @author liubin@njzfit.cn */
public class SecurityModelSpec extends FrameworkModelSpec {

  public SecurityModelSpec() {
    addActionExample();
  }

  private void addActionExample() {
    List<ActionSpec> actions = getActions();
    for (ActionSpec action : actions) {
      if (action instanceof DelayActionSpec) {
        action.setLongDesc("SpringSecurity delay experiment");
        action.setExample(
            "# Do a delay 2s experiment for SpringSecurity login operation\n"
                + "blade create security delay --username admin --time 2000\n\n");
      }
      if (action instanceof ThrowCustomExceptionActionSpec) {
        action.setLongDesc("SpringSecurity throws customer exception experiment");
        action.setExample(
            "# Do a throws customer exception experiment for SpringSecurity login\n"
                + "blade create security throwCustomException --exception java.lang.Exception --username admin");
      }
    }
  }

  @Override
  protected List<MatcherSpec> createNewMatcherSpecs() {
    ArrayList<MatcherSpec> matcherSpecs = new ArrayList<MatcherSpec>();
    matcherSpecs.add(new SecurityMatcherSpec());
    return matcherSpecs;
  }

  @Override
  public String getTarget() {
    return SecurityConstant.PLUGIN_NAME;
  }

  @Override
  public String getShortDesc() {
    return "SpringSecurity login experiment";
  }

  @Override
  public String getLongDesc() {
    return "SpringSecurity login experiment contains delay and exception by command and so on";
  }
}
