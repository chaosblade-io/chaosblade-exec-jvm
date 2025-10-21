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

package com.alibaba.chaosblade.exec.plugin.tars.client;

import com.alibaba.chaosblade.exec.common.aop.PointCut;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.ClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.NameClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.*;

/**
 * @author saikei
 * @email lishiji@huya.com
 */
public class TarsClientPointCut implements PointCut {

  @Override
  public ClassMatcher getClassMatcher() {
    return new NameClassMatcher("com.qq.tars.client.rpc.tars.TarsInvoker");
  }

  @Override
  public MethodMatcher getMethodMatcher() {
    return new ParameterMethodMatcher(
        new String[] {"com.qq.tars.client.rpc.ServantInvokeContext"},
        1,
        ParameterMethodMatcher.EQUAL);
  }
}
