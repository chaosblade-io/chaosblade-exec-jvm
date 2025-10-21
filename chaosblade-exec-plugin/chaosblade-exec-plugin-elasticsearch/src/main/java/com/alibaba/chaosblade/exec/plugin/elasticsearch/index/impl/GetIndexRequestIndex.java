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

package com.alibaba.chaosblade.exec.plugin.elasticsearch.index.impl;

import com.alibaba.chaosblade.exec.plugin.elasticsearch.index.AbstractRequestIndex;
import java.util.Arrays;
import java.util.List;

/**
 * @Author Yuhan Tang
 *
 * @package: com.alibaba.chaosblade.exec.plugin.elasticsearch.index.impl @Date 2020-10-30 16:16
 */
public class GetIndexRequestIndex extends AbstractRequestIndex {
  @Override
  public String getName() {
    return "getIndex";
  }

  @Override
  public List<String> getIndex0(Object target) throws Exception {
    return Arrays.asList(index(target, "indices"));
  }
}
