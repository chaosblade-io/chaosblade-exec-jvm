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

package com.alibaba.chaosblade.exec.plugin.postgrelsql;

import com.alibaba.chaosblade.exec.common.model.FrameworkModelSpec;
import com.alibaba.chaosblade.exec.common.model.action.ActionSpec;
import com.alibaba.chaosblade.exec.common.model.action.delay.DelayActionSpec;
import com.alibaba.chaosblade.exec.common.model.action.exception.ThrowCustomExceptionActionSpec;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherSpec;
import java.util.ArrayList;
import java.util.List;

/** @author guoping.yao <a href="mailto:bryan880901@qq.com"> */
public class PostgrelsqlModelSpec extends FrameworkModelSpec {

  public PostgrelsqlModelSpec() {
    addActionExample();
  }

  private void addActionExample() {
    List<ActionSpec> actions = getActions();
    for (ActionSpec action : actions) {
      if (action instanceof DelayActionSpec) {
        action.setLongDesc("Postgrelsql delay experiment");
        action.setExample(
            "# Do a delay 2s experiment for Postgrelsql client INSERT statement\n"
                + "blade create psql delay --sqltype insert --time 4000");
      }
      if (action instanceof ThrowCustomExceptionActionSpec) {
        action.setLongDesc("Postgrelsql throws custom exception experiment");
        action.setExample(
            "# Do a throws custom exception experiment for Postgrelsql client INSERT statement\n"
                + "blade create psql throwCustomException --sqltype insert --exception java.lang.Exception");
      }
    }
  }

  @Override
  protected List<MatcherSpec> createNewMatcherSpecs() {
    ArrayList<MatcherSpec> matcherSpecs = new ArrayList<MatcherSpec>();
    matcherSpecs.add(new PostgrelsqlHostMatcherSpec());
    matcherSpecs.add(new PostgrelsqlTableMatcherSpec());
    matcherSpecs.add(new PostgrelsqlDatabaseMatcherSpec());
    matcherSpecs.add(new PostgrelsqlSqlTypeMatcherSpec());
    matcherSpecs.add(new PostgrelsqlPortMatcherSpec());
    return matcherSpecs;
  }

  @Override
  public String getTarget() {
    return PostgrelsqlConstant.TARGET_NAME;
  }

  @Override
  public String getShortDesc() {
    return "Postgrelsql experiment";
  }

  @Override
  public String getLongDesc() {
    return "Postgrelsql experiment contains delay and exception by table name and so on.";
  }
}
