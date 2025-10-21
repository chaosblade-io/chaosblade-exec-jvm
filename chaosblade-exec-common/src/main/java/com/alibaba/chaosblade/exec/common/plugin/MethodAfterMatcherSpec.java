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

package com.alibaba.chaosblade.exec.common.plugin;

import com.alibaba.chaosblade.exec.common.aop.PredicateResult;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherModel;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherSpec;

/** @author Changjun Xiao */
public class MethodAfterMatcherSpec implements MatcherSpec {

  @Override
  public String getName() {
    return MethodConstant.AFTER_METHOD_FLAG;
  }

  @Override
  public String getDesc() {
    return "Specify the method after event";
  }

  @Override
  public boolean noArgs() {
    return true;
  }

  @Override
  public boolean required() {
    return false;
  }

  @Override
  public PredicateResult predicate(MatcherModel matcherModel) {
    return PredicateResult.success();
  }
}
