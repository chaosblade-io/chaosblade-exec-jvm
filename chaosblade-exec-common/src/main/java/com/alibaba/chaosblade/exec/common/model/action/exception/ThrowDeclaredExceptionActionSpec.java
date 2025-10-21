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

package com.alibaba.chaosblade.exec.common.model.action.exception;

import com.alibaba.chaosblade.exec.common.aop.PredicateResult;
import com.alibaba.chaosblade.exec.common.constant.CategoryConstants;
import com.alibaba.chaosblade.exec.common.model.FlagSpec;
import com.alibaba.chaosblade.exec.common.model.action.ActionModel;
import com.alibaba.chaosblade.exec.common.model.action.BaseActionSpec;
import java.util.Collections;
import java.util.List;

/** @author Changjun Xiao */
public class ThrowDeclaredExceptionActionSpec extends BaseActionSpec {

  private static final FlagSpec exceptionMessageFlag = new ExceptionMessageFlagSpec();

  public ThrowDeclaredExceptionActionSpec() {
    super(new DefaultThrowExceptionExecutor(null, exceptionMessageFlag));
  }

  @Override
  public String getName() {
    return ThrowExceptionExecutor.THROW_DECLARED_EXCEPTION;
  }

  @Override
  public String[] getAliases() {
    return new String[] {"tde"};
  }

  @Override
  public String getShortDesc() {
    return "Throw the first declared exception of method";
  }

  @Override
  public String getLongDesc() {
    return "Throw the first declared exception of method";
  }

  @Override
  public List<FlagSpec> getActionFlags() {
    return Collections.singletonList(exceptionMessageFlag);
  }

  @Override
  public PredicateResult predicate(ActionModel actionModel) {
    return PredicateResult.success();
  }

  @Override
  public String[] getCategories() {
    return new String[] {CategoryConstants.JAVA_CUSTOM, CategoryConstants.JAVA_EXCEPTION};
  }
}
