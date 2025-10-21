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

package com.alibaba.chaosblade.exec.plugin.jedis;

import com.alibaba.chaosblade.exec.common.model.FrameworkModelSpec;
import com.alibaba.chaosblade.exec.common.model.action.ActionSpec;
import com.alibaba.chaosblade.exec.common.model.action.delay.DelayActionSpec;
import com.alibaba.chaosblade.exec.common.model.action.exception.ThrowCustomExceptionActionSpec;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherSpec;
import java.util.ArrayList;
import java.util.List;

/** @author guoping.yao <a href="mailto:bryan880901@qq.com"> */
public class JedisModelSpec extends FrameworkModelSpec {

  public JedisModelSpec() {
    addActionExample();
  }

  private void addActionExample() {
    List<ActionSpec> actions = getActions();
    for (ActionSpec action : actions) {
      if (action instanceof DelayActionSpec) {
        action.setLongDesc("Jedis commands delay experiments");
        action.setExample(
            "# Do a delay 2s experiment on Jedis `hset key name lina` command\n"
                + "blade create jedis delay --cmd hset --key name --time 2000\n\n"
                + "#Do a delay 2s experiment on Jedis `key name lina` command\n"
                + "blade create jedis delay --key name --time 2000");
      }
      if (action instanceof ThrowCustomExceptionActionSpec) {
        action.setLongDesc("Jedis commands throws custom exception experiments");
        action.setExample(
            "# Do a throws custom exception experiment on Jedis `key name lina` command\n"
                + "blade create jedis throwCustomException --exception java.lang.Exception --key name");
      }
    }
  }

  @Override
  protected List<MatcherSpec> createNewMatcherSpecs() {
    ArrayList<MatcherSpec> matcherSpecs = new ArrayList<MatcherSpec>();
    //  matcherSpecs.add(new JedisHostMatcherSpec());
    //  matcherSpecs.add(new JedisPortMatcherSpec());
    //  matcherSpecs.add(new JedisDatabaseMatcherSpec());
    matcherSpecs.add(new JedisCmdTypeMatcherSpec());
    matcherSpecs.add(new JedisKeyMatcherSpec());
    return matcherSpecs;
  }

  @Override
  public String getTarget() {
    return JedisConstant.TARGET_NAME;
  }

  @Override
  public String getShortDesc() {
    return "jedis experiment";
  }

  @Override
  public String getLongDesc() {
    return "jedis experiment contains delay and exception by command and so on.";
  }
}
