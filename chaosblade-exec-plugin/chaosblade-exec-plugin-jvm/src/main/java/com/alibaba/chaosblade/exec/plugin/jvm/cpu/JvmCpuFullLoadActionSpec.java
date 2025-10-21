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

package com.alibaba.chaosblade.exec.plugin.jvm.cpu;

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

/** @author Changjun Xiao */
public class JvmCpuFullLoadActionSpec extends BaseActionSpec implements DirectlyInjectionAction {

  public JvmCpuFullLoadActionSpec() {
    super(new JvmCpuFullLoadExecutor());
  }

  @Override
  public String getName() {
    return JvmConstant.ACTION_CPU_FULL_LOAD_NAME;
  }

  @Override
  public String[] getAliases() {
    return new String[] {JvmConstant.ACTION_CPU_FULL_LOAD_ALIAS};
  }

  @Override
  public String getShortDesc() {
    return "Process occupied cpu full load";
  }

  @Override
  public String getLongDesc() {
    return "Process occupied cpu full load";
  }

  @Override
  public List<FlagSpec> getActionFlags() {
    ArrayList<FlagSpec> flagSpecs = new ArrayList<FlagSpec>();
    flagSpecs.add(new CpuCountFlagSpec());
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
    return "# Specifies full load of all kernel\n"
        + "blade c jvm cfl --process tomcat\n\n"
        + "# Specifies full load of two kernel\n"
        + "blade c jvm cfl --cpu-count 2 --process tomcat";
  }

  @Override
  public String[] getCategories() {
    return new String[] {CategoryConstants.JAVA_RESOURCE_CPU};
  }
}
