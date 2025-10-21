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

import static com.alibaba.chaosblade.exec.plugin.lettuce.LettuceConstants.CMD;

import com.alibaba.chaosblade.exec.common.model.matcher.BasePredicateMatcherSpec;

/**
 * @author yefei
 * @create 2020-11-23 14:53
 */
public class LettuceCmdMatcherSpec extends BasePredicateMatcherSpec {

  @Override
  public String getName() {
    return CMD;
  }

  @Override
  public String getDesc() {
    return "cmd matcher";
  }

  @Override
  public boolean noArgs() {
    return false;
  }

  @Override
  public boolean required() {
    return false;
  }
}
