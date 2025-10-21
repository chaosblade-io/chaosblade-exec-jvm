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

package com.alibaba.chaosblade.exec.plugin.zookeeper;

import com.alibaba.chaosblade.exec.common.model.FrameworkModelSpec;
import com.alibaba.chaosblade.exec.common.model.action.ActionSpec;
import com.alibaba.chaosblade.exec.common.model.action.delay.DelayActionSpec;
import com.alibaba.chaosblade.exec.common.model.action.exception.ThrowCustomExceptionActionSpec;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherSpec;
import java.util.ArrayList;
import java.util.List;

/** @author liuhq @Date 2020/11/23 上午11:36 */
public class ZookeeperModelSpec extends FrameworkModelSpec {

  public ZookeeperModelSpec() {
    addActionExample();
  }

  private void addActionExample() {
    List<ActionSpec> actions = getActions();
    for (ActionSpec action : actions) {
      if (action instanceof DelayActionSpec) {
        action.setLongDesc("zk commands delay experiments");
        action.setExample(
            "# Do a delay 2s experiment on zk `path` command\n"
                + "blade create zk delay  --path /test --time 2000\n");
      }
      if (action instanceof ThrowCustomExceptionActionSpec) {
        action.setLongDesc("zk commands throws custom exception experiments");
        action.setExample(
            "# Do a throws custom exception experiment on zk `path ` command\n"
                + "blade create zk throwCustomException --exception java.lang.Exception --path /test");
      }
    }
  }

  @Override
  protected List<MatcherSpec> createNewMatcherSpecs() {
    ArrayList<MatcherSpec> matcherSpecs = new ArrayList<MatcherSpec>();
    matcherSpecs.add(new ZookeeperPathMatcherSpec());
    return matcherSpecs;
  }

  @Override
  public String getTarget() {
    return ZookeeperConstant.TARGET_NAME;
  }

  @Override
  public String getShortDesc() {
    return "zk experiment";
  }

  @Override
  public String getLongDesc() {
    return "zk experiment contains delay and exception by command and so on.";
  }
}
