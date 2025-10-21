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

package com.alibaba.chaosblade.exec.plugin.jvm.gc;

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
import java.util.ArrayList;
import java.util.List;

/** @author shizhi.zhu@qunar.com */
public class FullGCActionSpec extends BaseActionSpec implements DirectlyInjectionAction {

  public FullGCActionSpec() {
    super(new FullGCExecutor());
  }

  @Override
  public String getName() {
    return JvmConstant.ACTION_FULL_GC_NAME;
  }

  @Override
  public String[] getAliases() {
    return new String[] {JvmConstant.ACTION_FULL_GC_ALIAS};
  }

  @Override
  public String getShortDesc() {
    return "JVM full gc";
  }

  @Override
  public String getLongDesc() {
    return "JVM full gc";
  }

  @Override
  public List<FlagSpec> getActionFlags() {
    List<FlagSpec> flagSpecs = new ArrayList<FlagSpec>();
    flagSpecs.add(new FullGCIntervalFlagSpec());
    flagSpecs.add(new FullGCEffectCountFlagSpec());
    return flagSpecs;
  }

  @Override
  public PredicateResult predicate(ActionModel actionModel) {
    return PredicateResult.success();
  }

  @Override
  public String getExample() {
    return "# Specifies full gc\n" + "blade c jvm fgc --effect-count 100 --interval 1000\n\n";
  }

  @Override
  public String[] getCategories() {
    return new String[] {CategoryConstants.JAVA_RESOURCE_MEMORY};
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
}
