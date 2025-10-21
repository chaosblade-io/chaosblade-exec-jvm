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

package com.alibaba.chaosblade.exec.common.model.action.threadpool;

import com.alibaba.chaosblade.exec.common.aop.PredicateResult;
import com.alibaba.chaosblade.exec.common.constant.CategoryConstants;
import com.alibaba.chaosblade.exec.common.model.FlagSpec;
import com.alibaba.chaosblade.exec.common.model.action.ActionExecutor;
import com.alibaba.chaosblade.exec.common.model.action.ActionModel;
import com.alibaba.chaosblade.exec.common.model.action.BaseActionSpec;
import com.alibaba.chaosblade.exec.common.util.StringUtils;
import java.util.ArrayList;
import java.util.List;

/** @author Changjun Xiao */
public class ThreadPoolFullActionSpec extends BaseActionSpec {

  public static final String NAME = "threadpoolfull";

  public ThreadPoolFullActionSpec() {
    super(new DefaultThreadPoolFullExecutor());
  }

  public ThreadPoolFullActionSpec(ActionExecutor actionExecutor) {
    super(actionExecutor);
  }

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public String[] getAliases() {
    return new String[] {"tpf"};
  }

  @Override
  public String getShortDesc() {
    return "Thread pool full";
  }

  @Override
  public String getLongDesc() {
    if (StringUtils.isNotBlank(super.getLongDesc())) {
      return super.getLongDesc();
    }
    return "Thread pool full";
  }

  @Override
  public List<FlagSpec> getActionFlags() {
    return new ArrayList<FlagSpec>();
  }

  @Override
  public PredicateResult predicate(ActionModel actionModel) {
    return PredicateResult.success();
  }

  @Override
  public String[] getCategories() {
    return new String[] {CategoryConstants.JAVA_RESOURCE_CPU};
  }
}
