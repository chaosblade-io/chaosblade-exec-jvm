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

package com.alibaba.chaosblade.exec.plugin.redisson;

import com.alibaba.chaosblade.exec.common.model.FrameworkModelSpec;
import com.alibaba.chaosblade.exec.common.model.action.ActionSpec;
import com.alibaba.chaosblade.exec.common.model.action.delay.DelayActionSpec;
import com.alibaba.chaosblade.exec.common.model.action.exception.ThrowCustomExceptionActionSpec;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherSpec;
import java.util.ArrayList;
import java.util.List;

/** @author xueshaoyi @Date 2020/11/23 上午11:36 */
public class RedissonModelSpec extends FrameworkModelSpec {

  public RedissonModelSpec() {
    addActionExample();
  }

  private void addActionExample() {
    List<ActionSpec> actions = getActions();
    for (ActionSpec action : actions) {
      if (action instanceof DelayActionSpec) {
        action.setLongDesc("Redisson commands delay experiments");
        action.setExample(
            "# Do a delay 2s experiment on Redisson `hset key name lina` command\n"
                + "blade create redisson delay --cmd hset --key name --time 2000\n\n"
                + "#Do a delay 2s experiment on Redisson `key name lina` command\n"
                + "blade create redisson delay --key name --time 2000");
      }
      if (action instanceof ThrowCustomExceptionActionSpec) {
        action.setLongDesc("Redisson commands throws custom exception experiments");
        action.setExample(
            "# Do a throws custom exception experiment on Redisson `key name lina` command\n"
                + "blade create redisson throwCustomException --exception java.lang.Exception --key name");
      }
    }
  }

  @Override
  protected List<MatcherSpec> createNewMatcherSpecs() {
    ArrayList<MatcherSpec> matcherSpecs = new ArrayList<MatcherSpec>();
    matcherSpecs.add(new RedissonCmdTypeMatcherSpec());
    matcherSpecs.add(new RedissonKeyMatcherSpec());
    return matcherSpecs;
  }

  @Override
  public String getTarget() {
    return RedissonConstant.TARGET_NAME;
  }

  @Override
  public String getShortDesc() {
    return "redisson experiment";
  }

  @Override
  public String getLongDesc() {
    return "redisson experiment contains delay and exception by command and so on.";
  }
}
