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

package com.alibaba.chaosblade.exec.plugin.gateway;

import com.alibaba.chaosblade.exec.common.model.FrameworkModelSpec;
import com.alibaba.chaosblade.exec.common.model.action.ActionSpec;
import com.alibaba.chaosblade.exec.common.model.action.delay.DelayActionSpec;
import com.alibaba.chaosblade.exec.common.model.action.exception.ThrowCustomExceptionActionSpec;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherSpec;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author wb-shd671576
 *
 * @package: com.alibaba.chaosblade.exec.plugin.gateway @Date 2021-07-29
 */
public class GatewayModelSpec extends FrameworkModelSpec {

  public GatewayModelSpec() {
    addActionExample();
  }

  private void addActionExample() {
    List<ActionSpec> actions = getActions();
    for (ActionSpec action : actions) {
      if (action instanceof DelayActionSpec) {
        action.setLongDesc("SpringCloud Gateway delay experiment");
        action.setExample(
            "# Do a delay 2s experiment for SpringCloud Gateway forward operations\n"
                + "blade create gateway delay --requestPath /gateway/path --time 2000\n\n");
      }
      if (action instanceof ThrowCustomExceptionActionSpec) {
        action.setLongDesc("SpringCloudGateway throws customer exception experiment");
        action.setExample(
            "# Do a throws customer exception experiment for SpringCloud Gateway operations\n"
                + "blade create gateway throwCustomException --exception java.lang.Exception --requestPath /gateway/path");
      }
    }
  }

  @Override
  protected List<MatcherSpec> createNewMatcherSpecs() {
    ArrayList<MatcherSpec> matcherSpecs = new ArrayList<MatcherSpec>();
    matcherSpecs.add(new GatewayPathMatcherSpec());
    return matcherSpecs;
  }

  @Override
  public String getTarget() {
    return GatewayConstant.TARGET_NAME;
  }

  @Override
  public String getShortDesc() {
    return "gateway experiment!";
  }

  @Override
  public String getLongDesc() {
    return "gateway experiment contains delay and exception";
  }
}
