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

/** @author Changjun Xiao */
public abstract class BasePredicateMatcherSpec implements MatcherSpec {

  @Override
  public PredicateResult predicate(MatcherModel matcherModel) {
    String value = matcherModel.get(getName());
    if (value == null) {
      if (required()) {
        return PredicateResult.fail("less necessary " + getName() + " value");
      }
    }
    return PredicateResult.success();
  }
}
