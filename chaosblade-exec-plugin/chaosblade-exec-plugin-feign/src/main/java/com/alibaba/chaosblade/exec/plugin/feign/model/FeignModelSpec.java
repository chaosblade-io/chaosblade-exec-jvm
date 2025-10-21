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

package com.alibaba.chaosblade.exec.plugin.feign.model;

import com.alibaba.chaosblade.exec.common.aop.PredicateResult;
import com.alibaba.chaosblade.exec.common.model.FrameworkModelSpec;
import com.alibaba.chaosblade.exec.common.model.Model;
import com.alibaba.chaosblade.exec.common.model.action.ActionSpec;
import com.alibaba.chaosblade.exec.common.model.action.delay.DelayActionSpec;
import com.alibaba.chaosblade.exec.common.model.action.exception.ThrowCustomExceptionActionSpec;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherModel;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherSpec;
import com.alibaba.chaosblade.exec.plugin.feign.FeignConstant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/** @author guoyu486@gmail.com */
public class FeignModelSpec extends FrameworkModelSpec implements FeignConstant {

  public FeignModelSpec() {
    addActionExample();
  }

  private void addActionExample() {
    List<ActionSpec> actions = getActions();
    for (ActionSpec action : actions) {
      if (action instanceof DelayActionSpec) {
        action.setLongDesc("feign delay experiment");
        action.setExample(
            "# Delay when service call api time\n"
                + "blade create feign delay --time 3000 --service-name test-service --url /example/feign/api ");
      } else if (action instanceof ThrowCustomExceptionActionSpec) {
        action.setLongDesc("Feign throws custom exception experiment");
        action.setExample(
            "# Throw exception when service call api \n"
                + "blade create feign throwCustomException --exception java.lang.RuntimeException --exception-message "
                + "mock-beans-exception --service-name test-service --url /example/feign/api");
      }
    }
  }

  @Override
  protected List<MatcherSpec> createNewMatcherSpecs() {
    ArrayList<MatcherSpec> arrayList = new ArrayList<MatcherSpec>();
    arrayList.add(new ServiceNameMatcherSpec());
    arrayList.add(new UrlMatcherSpec());
    return arrayList;
  }

  @Override
  public String getTarget() {
    return TARGET_NAME;
  }

  @Override
  public String getShortDesc() {
    return "feign experiment";
  }

  @Override
  public String getLongDesc() {
    return "feign experiment for testing service api delay and exception.";
  }

  @Override
  protected PredicateResult preMatcherPredicate(Model model) {
    if (model == null) {
      return PredicateResult.fail("matcher not found for feign");
    }
    MatcherModel matcher = model.getMatcher();
    Set<String> keySet = matcher.getMatchers().keySet();
    if (keySet.contains(SERVICE_NAME) || keySet.contains(TEMPLATE_URL)) {
      return PredicateResult.success();
    } else {
      return PredicateResult.fail("feign chaos require param [serviceName,url] ");
    }
  }
}
