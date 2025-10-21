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

package com.alibaba.chaosblade.exec.plugin.hbase;

import com.alibaba.chaosblade.exec.common.model.FrameworkModelSpec;
import com.alibaba.chaosblade.exec.common.model.action.ActionSpec;
import com.alibaba.chaosblade.exec.common.model.action.delay.DelayActionSpec;
import com.alibaba.chaosblade.exec.common.model.action.exception.ThrowCustomExceptionActionSpec;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherSpec;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author <a href="tangyuhan@shulie.io">yuhan.tang</a>
 *
 * @package: com.alibaba.chaosblade.exec.plugin.hbase @Date 2020-10-30 14:10
 */
public class HbaseModelSpec extends FrameworkModelSpec {

  public HbaseModelSpec() {
    addActionExample();
  }

  private void addActionExample() {
    List<ActionSpec> actions = getActions();
    for (ActionSpec action : actions) {
      if (action instanceof DelayActionSpec) {
        action.setLongDesc("hbase delay experiment");
        action.setExample(
            "# Do a delay 2s experiment for hbase client connection INSERT statement\n"
                + "blade create hbase delay --table table1\n\n"
                + "#Do a delay 2s experiment on hbase column\n"
                + "blade create hbase delay --column column1");
      }
      if (action instanceof ThrowCustomExceptionActionSpec) {
        action.setLongDesc("hbase throws customer exception experiment");
        action.setExample(
            "# Do a throws customer exception experiment for mysql client connection port=3306 INSERT statement\n"
                + "blade create hbase throwCustomException --exception java.lang.Exception --table table2\n\n"
                + "#Do a throws customer exception experiment on hbase column\n"
                + "blade create hbase throwCustomException --exception java.lang.Exception --column column2");
      }
    }
  }

  @Override
  protected List<MatcherSpec> createNewMatcherSpecs() {
    ArrayList<MatcherSpec> matcherSpecs = new ArrayList<MatcherSpec>();
    matcherSpecs.add(new HbaseTableMatcherSpec());
    matcherSpecs.add(new HbaseColumnMatcherSpec());
    return matcherSpecs;
  }

  @Override
  public String getTarget() {
    return HbaseConstant.TARGET_NAME;
  }

  @Override
  public String getShortDesc() {
    return "hbase experiment!";
  }

  @Override
  public String getLongDesc() {
    return "hbase experiment contains delay and exception by table name and so on.";
  }
}
