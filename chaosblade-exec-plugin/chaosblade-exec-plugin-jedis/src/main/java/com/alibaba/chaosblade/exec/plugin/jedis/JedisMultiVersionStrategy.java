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

package com.alibaba.chaosblade.exec.plugin.jedis;

import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;

/** @author liuhq <a href="15669072513@163.com"> */
public interface JedisMultiVersionStrategy {
  /**
   * jedis multi version strategy process
   *
   * @param classLoader classLoader
   * @param methodArguments jedis method argument array
   * @return EnhancerModel
   * @throws Exception
   */
  EnhancerModel process(ClassLoader classLoader, Object[] methodArguments) throws Exception;
}
