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

package com.alibaba.chaosblade.exec.plugin.lettuce;

import com.alibaba.chaosblade.exec.common.aop.PredicateResult;
import com.alibaba.chaosblade.exec.common.constant.CategoryConstants;
import com.alibaba.chaosblade.exec.common.model.FlagSpec;
import com.alibaba.chaosblade.exec.common.model.action.ActionModel;
import com.alibaba.chaosblade.exec.common.model.action.BaseActionSpec;
import com.alibaba.chaosblade.exec.common.util.StringUtil;
import java.util.ArrayList;
import java.util.List;

/** @author yefei */
public class UpdateActionSpec extends BaseActionSpec {

  private static final LettuceValueFlagSpec VALUE_FLAG_SPEC = new LettuceValueFlagSpec();

  public UpdateActionSpec() {
    super(new LettuceActionExecutor(VALUE_FLAG_SPEC));
  }

  @Override
  public String getName() {
    return "update";
  }

  @Override
  public String[] getAliases() {
    return new String[0];
  }

  @Override
  public String getShortDesc() {
    return "update action spec";
  }

  @Override
  public String getLongDesc() {
    return "update action spec";
  }

  @Override
  public List<FlagSpec> getActionFlags() {
    List<FlagSpec> flagSpecs = new ArrayList<FlagSpec>();
    flagSpecs.add(VALUE_FLAG_SPEC);
    return flagSpecs;
  }

  @Override
  public PredicateResult predicate(ActionModel actionModel) {
    if (StringUtil.isBlank(actionModel.getFlag(VALUE_FLAG_SPEC.getName()))) {
      return PredicateResult.fail("less value argument");
    }
    return PredicateResult.success();
  }

  @Override
  public String[] getCategories() {
    return new String[] {CategoryConstants.JAVA_DATA_TAMPER};
  }
}
