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

package com.alibaba.chaosblade.exec.plugin.jvm.thread.model;

import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.common.aop.PredicateResult;
import com.alibaba.chaosblade.exec.common.constant.CategoryConstants;
import com.alibaba.chaosblade.exec.common.model.FlagSpec;
import com.alibaba.chaosblade.exec.common.model.Model;
import com.alibaba.chaosblade.exec.common.model.action.ActionModel;
import com.alibaba.chaosblade.exec.common.model.action.BaseActionSpec;
import com.alibaba.chaosblade.exec.common.model.action.DirectlyInjectionAction;
import com.alibaba.chaosblade.exec.plugin.jvm.JvmConstant;
import com.alibaba.chaosblade.exec.plugin.jvm.StoppableActionExecutor;
import com.alibaba.chaosblade.exec.plugin.jvm.thread.JvmThreadPoolFullExecutor;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author Yuhan Tang
 *
 * @package: com.alibaba.chaosblade.exec.plugin.jvm.thread @Date 2020-11-02 13:38
 */
public class JvmThreadFullActionSpec extends BaseActionSpec implements DirectlyInjectionAction {

  public JvmThreadFullActionSpec() {
    super(new JvmThreadPoolFullExecutor());
  }

  @Override
  public String getName() {
    return JvmConstant.THREAD_FULL;
  }

  @Override
  public String[] getAliases() {
    return new String[] {JvmConstant.ACTION_THREAD_FULL_ALIAS};
  }

  @Override
  public String getShortDesc() {
    return "Specifies that the application thread is soaring";
  }

  @Override
  public String getLongDesc() {
    return "Specifies that the application thread is soaring";
  }

  @Override
  public List<FlagSpec> getActionFlags() {
    ArrayList<FlagSpec> flagSpecs = new ArrayList<FlagSpec>();
    flagSpecs.add(new JvmThreadCountSpec());
    flagSpecs.add(new ThreadRunningMatcherSpec());
    flagSpecs.add(new ThreadWaitMatcherSpec());
    return flagSpecs;
  }

  @Override
  public PredicateResult predicate(ActionModel actionModel) {
    return PredicateResult.success();
  }

  @Override
  public void createInjection(String uid, Model model) throws Exception {
    EnhancerModel enhancerModel =
        new EnhancerModel(EnhancerModel.class.getClassLoader(), model.getMatcher());
    enhancerModel.merge(model);
    getActionExecutor().run(enhancerModel);
  }

  @Override
  public void destroyInjection(String uid, Model model) throws Exception {
    EnhancerModel enhancerModel =
        new EnhancerModel(EnhancerModel.class.getClassLoader(), model.getMatcher());
    enhancerModel.merge(model);
    ((StoppableActionExecutor) getActionExecutor()).stop(enhancerModel);
  }

  @Override
  public String getExample() {
    return "# Specifies that the application thread is soaring and the status wait\n"
        + "blade create jvm threadfull --wait --thread-count 20\n\n"
        + "#Specifies that the application thread is soaring and running status\n"
        + "blade create jvm threadfull --running --thread-count 20";
  }

  @Override
  public String[] getCategories() {
    return new String[] {CategoryConstants.JAVA_RESOURCE_CPU};
  }
}
