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

package com.alibaba.chaosblade.exec.common.model.matcher;

import com.alibaba.chaosblade.exec.common.aop.PredicateResult;
import com.alibaba.chaosblade.exec.common.constant.ModelConstant;
import com.alibaba.chaosblade.exec.common.util.StringUtil;

/** @author Changjun Xiao */
public class EffectPercentMatcherSpec implements MatcherSpec {

  @Override
  public String getName() {
    return ModelConstant.EFFECT_PERCENT_MATCHER_NAME;
  }

  @Override
  public String getDesc() {
    return "The percent of chaos experiment in effect";
  }

  @Override
  public boolean noArgs() {
    return false;
  }

  @Override
  public boolean required() {
    return false;
  }

  @Override
  public PredicateResult predicate(MatcherModel matcherModel) {
    String percent = matcherModel.get(ModelConstant.EFFECT_PERCENT_MATCHER_NAME);
    if (!StringUtil.isBlank(percent)) {
      try {
        Integer.valueOf(percent);
      } catch (NumberFormatException e) {
        return PredicateResult.fail(
            ModelConstant.EFFECT_PERCENT_MATCHER_NAME + " value is illegal: " + percent);
      }
    }
    return PredicateResult.success();
  }
}
