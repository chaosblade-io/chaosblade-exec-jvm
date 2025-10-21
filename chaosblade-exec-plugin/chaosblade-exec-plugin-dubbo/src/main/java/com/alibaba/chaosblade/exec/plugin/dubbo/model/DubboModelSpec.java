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

package com.alibaba.chaosblade.exec.plugin.dubbo.model;

import com.alibaba.chaosblade.exec.common.aop.PredicateResult;
import com.alibaba.chaosblade.exec.common.exception.ExperimentException;
import com.alibaba.chaosblade.exec.common.model.FrameworkModelSpec;
import com.alibaba.chaosblade.exec.common.model.Model;
import com.alibaba.chaosblade.exec.common.model.action.ActionExecutor;
import com.alibaba.chaosblade.exec.common.model.action.ActionSpec;
import com.alibaba.chaosblade.exec.common.model.action.delay.DelayActionSpec;
import com.alibaba.chaosblade.exec.common.model.action.exception.ThrowCustomExceptionActionSpec;
import com.alibaba.chaosblade.exec.common.model.action.threadpool.ThreadPoolFullActionSpec;
import com.alibaba.chaosblade.exec.common.model.handler.PreCreateInjectionModelHandler;
import com.alibaba.chaosblade.exec.common.model.handler.PreDestroyInjectionModelHandler;
import com.alibaba.chaosblade.exec.common.model.matcher.BusinessParamsMatcherSpec;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherModel;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherSpec;
import com.alibaba.chaosblade.exec.common.plugin.MethodNameMatcherSpec;
import com.alibaba.chaosblade.exec.plugin.dubbo.DubboConstant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/** @author Changjun Xiao */
public class DubboModelSpec extends FrameworkModelSpec
    implements PreCreateInjectionModelHandler, PreDestroyInjectionModelHandler {

  public DubboModelSpec() {
    super();
    addThreadPoolFullActionSpec();
    addActionExample();
  }

  private void addActionExample() {
    List<ActionSpec> actions = getActions();
    for (ActionSpec action : actions) {
      if (action instanceof DelayActionSpec) {
        action.setLongDesc(
            "Dubbo interface to do delay experiments, support provider and consumer");
        action.setExample(
            "# Invoke com.alibaba.demo.HelloService.hello() service, do delay 3 seconds experiment\n"
                + "blade create dubbo delay --time 3000 --service com.alibaba.demo.HelloService --methodname hello --consumer");
      } else if (action instanceof ThrowCustomExceptionActionSpec) {
        action.setLongDesc(
            "Dubbo interface to do throws custom exception experiments, support provider and consumer");
        action.setExample(
            "# Invoke com.alibaba.demo.HelloService.hello() service, do throws customer exception\n"
                + "blade create dubbo throwCustomException --exception java.lang.Exception --service com.alibaba.demo.HelloService --methodname hello --consumer");
      } else if (action instanceof ThreadPoolFullActionSpec) {
        action.setLongDesc("Do a full load experiment on the Dubbo provider thread pool");
        action.setExample(
            "# Do a full load experiment on the Dubbo provider thread pool\n"
                + "blade c dubbo threadpoolfull --provider");
      }
    }
  }

  @Override
  public String getShortDesc() {
    return "Experiment with the Dubbo";
  }

  @Override
  public String getLongDesc() {
    return "Dubbo experiment for testing service delay and exception.";
  }

  @Override
  protected List<MatcherSpec> createNewMatcherSpecs() {
    ArrayList<MatcherSpec> matcherSpecs = new ArrayList<MatcherSpec>();
    matcherSpecs.add(new ConsumerMatcherSpec());
    matcherSpecs.add(new ProviderMatcherSpec());
    matcherSpecs.add(new AppNameMatcherDefSpec());
    matcherSpecs.add(new ServiceMatcherSpec());
    matcherSpecs.add(new VersionMatcherSpec());
    matcherSpecs.add(new MethodNameMatcherSpec());
    matcherSpecs.add(new GroupMatcherSpec());
    matcherSpecs.add(new CallPointMatcherSpec());
    matcherSpecs.add(new BusinessParamsMatcherSpec());
    return matcherSpecs;
  }

  @Override
  protected PredicateResult preMatcherPredicate(Model model) {
    if (model == null) {
      return PredicateResult.fail("matcher not found for dubbo");
    }
    MatcherModel matcher = model.getMatcher();
    Set<String> keySet = matcher.getMatchers().keySet();
    for (String key : keySet) {
      if (key.equals(DubboConstant.CONSUMER_KEY) || key.equals(DubboConstant.PROVIDER_KEY)) {
        return PredicateResult.success();
      }
    }
    return PredicateResult.fail("less necessary matcher is consumer or provider for dubbo");
  }

  @Override
  public String getTarget() {
    return DubboConstant.TARGET_NAME;
  }

  @Override
  public void preCreate(String suid, Model model) throws ExperimentException {
    if (ThreadPoolFullActionSpec.NAME.equals(model.getActionName())) {
      ActionSpec actionSpec = this.getActionSpec(model.getActionName());
      ActionExecutor actionExecutor = actionSpec.getActionExecutor();
      if (actionExecutor instanceof DubboThreadPoolFullExecutor) {
        DubboThreadPoolFullExecutor threadPoolFullExecutor =
            (DubboThreadPoolFullExecutor) actionExecutor;
        threadPoolFullExecutor.setExpReceived(true);
      } else {
        throw new ExperimentException(
            "actionExecutor is not instance of DubboThreadPoolFullExecutor");
      }
    }
  }

  @Override
  public void preDestroy(String suid, Model model) throws ExperimentException {
    if (ThreadPoolFullActionSpec.NAME.equals(model.getActionName())) {
      ActionSpec actionSpec = this.getActionSpec(model.getActionName());
      ActionExecutor actionExecutor = actionSpec.getActionExecutor();
      if (actionExecutor instanceof DubboThreadPoolFullExecutor) {
        DubboThreadPoolFullExecutor threadPoolFullExecutor =
            (DubboThreadPoolFullExecutor) actionExecutor;
        threadPoolFullExecutor.revoke();
      } else {
        throw new ExperimentException(
            "actionExecutor is not instance of DubboThreadPoolFullExecutor");
      }
    }
  }

  private void addThreadPoolFullActionSpec() {
    ThreadPoolFullActionSpec threadPoolFullActionSpec =
        new ThreadPoolFullActionSpec(DubboThreadPoolFullExecutor.INSTANCE);
    addActionSpec(threadPoolFullActionSpec);
    // the thread pool full experiment applies to provider
    addMatcherDefToAllActions(new ProviderMatcherSpec());
  }
}
