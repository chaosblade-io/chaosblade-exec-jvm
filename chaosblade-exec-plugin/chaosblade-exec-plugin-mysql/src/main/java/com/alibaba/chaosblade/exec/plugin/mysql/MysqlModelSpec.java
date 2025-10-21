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

package com.alibaba.chaosblade.exec.plugin.mysql;

import com.alibaba.chaosblade.exec.common.model.FrameworkModelSpec;
import com.alibaba.chaosblade.exec.common.model.action.ActionSpec;
import com.alibaba.chaosblade.exec.common.model.action.delay.DelayActionSpec;
import com.alibaba.chaosblade.exec.common.model.action.exception.ThrowCustomExceptionActionSpec;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherSpec;
import java.util.ArrayList;
import java.util.List;

/** @author Changjun Xiao */
public class MysqlModelSpec extends FrameworkModelSpec {

  public MysqlModelSpec() {
    addActionExample();
  }

  private void addActionExample() {
    List<ActionSpec> actions = getActions();
    for (ActionSpec action : actions) {
      if (action instanceof DelayActionSpec) {
        action.setLongDesc("Mysql delay experiment");
        action.setExample(
            "# Do a delay 2s experiment for mysql client connection port=3306 INSERT statement\n"
                + "blade create mysql delay --time 2000 --sqltype select --port 3306");
      }
      if (action instanceof ThrowCustomExceptionActionSpec) {
        action.setLongDesc("Mysql throws customer exception experiment");
        action.setExample(
            "# Do a throws customer exception experiment for mysql client connection port=3306 INSERT statement\n"
                + "blade create mysql throwCustomException --exception java.lang.Exception");
      }
    }
  }

  @Override
  protected List<MatcherSpec> createNewMatcherSpecs() {
    ArrayList<MatcherSpec> matcherSpecs = new ArrayList<MatcherSpec>();
    matcherSpecs.add(new MysqlHostMatcherSpec());
    matcherSpecs.add(new MysqlTableMatcherSpec());
    matcherSpecs.add(new MysqlDatabaseMatcherSpec());
    matcherSpecs.add(new MysqlSqlTypeMatcherSpec());
    matcherSpecs.add(new MysqlPortMatcherSpec());
    return matcherSpecs;
  }

  @Override
  public String getTarget() {
    return MysqlConstant.TARGET_NAME;
  }

  @Override
  public String getShortDesc() {
    return "mysql experiment";
  }

  @Override
  public String getLongDesc() {
    return "Mysql experiment contains delay and exception by table name and so on.";
  }
}
