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

package com.alibaba.chaosblade.exec.common.model.action.returnv;

import com.alibaba.chaosblade.exec.common.aop.PredicateResult;
import com.alibaba.chaosblade.exec.common.constant.CategoryConstants;
import com.alibaba.chaosblade.exec.common.model.FlagSpec;
import com.alibaba.chaosblade.exec.common.model.action.ActionModel;
import com.alibaba.chaosblade.exec.common.model.action.BaseActionSpec;
import java.util.ArrayList;
import java.util.List;

/** @author Changjun Xiao */
public class ReturnValueActionSpec extends BaseActionSpec {

  private static ValueFlagSpec valueFlagSpec = new ValueFlagSpec();

  public ReturnValueActionSpec() {
    super(new DefaultReturnValueExecutor(valueFlagSpec));
  }

  @Override
  public String getName() {
    return "return";
  }

  @Override
  public String[] getAliases() {
    return new String[0];
  }

  @Override
  public String getShortDesc() {
    return "Return the specify value";
  }

  @Override
  public String getLongDesc() {
    return "Return the specify value";
  }

  public ValueFlagSpec getValueFlagSpec() {
    return valueFlagSpec;
  }

  @Override
  public List<FlagSpec> getActionFlags() {
    ArrayList<FlagSpec> flagSpecs = new ArrayList<FlagSpec>(1);
    flagSpecs.add(new ValueFlagSpec());
    return flagSpecs;
  }

  @Override
  public PredicateResult predicate(ActionModel actionModel) {
    return PredicateResult.success();
  }

  @Override
  public String[] getCategories() {
    return new String[] {CategoryConstants.JAVA_DATA_TAMPER};
  }
}
