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

package com.alibaba.chaosblade.exec.plugin.servlet.code;

import com.alibaba.chaosblade.exec.common.aop.PredicateResult;
import com.alibaba.chaosblade.exec.common.constant.CategoryConstants;
import com.alibaba.chaosblade.exec.common.model.FlagSpec;
import com.alibaba.chaosblade.exec.common.model.action.ActionModel;
import com.alibaba.chaosblade.exec.common.model.action.BaseActionSpec;
import com.alibaba.chaosblade.exec.common.util.StringUtil;
import java.util.ArrayList;
import java.util.List;

/** @author yefei */
public class ModifyHttpCodeActionSpec extends BaseActionSpec {

  private static final HttpCodeFlagSpec HTTP_CODE_FLAG_SPEC = new HttpCodeFlagSpec();

  public ModifyHttpCodeActionSpec() {
    super(new ModifyHttpCodeActionExecutor(HTTP_CODE_FLAG_SPEC));
  }

  @Override
  public String getName() {
    return "modifyCode";
  }

  @Override
  public String[] getAliases() {
    return new String[] {"mc"};
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
    flagSpecs.add(HTTP_CODE_FLAG_SPEC);
    return flagSpecs;
  }

  @Override
  public PredicateResult predicate(ActionModel actionModel) {
    if (StringUtil.isBlank(actionModel.getFlag(HTTP_CODE_FLAG_SPEC.getName()))) {
      return PredicateResult.fail("less http code argument");
    }
    return PredicateResult.success();
  }

  @Override
  public String[] getCategories() {
    return new String[] {CategoryConstants.JAVA_DATA_TAMPER};
  }
}
