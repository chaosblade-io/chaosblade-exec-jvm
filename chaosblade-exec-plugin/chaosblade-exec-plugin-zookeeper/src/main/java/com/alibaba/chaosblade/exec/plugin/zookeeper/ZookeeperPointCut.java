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

package com.alibaba.chaosblade.exec.plugin.zookeeper;

import static com.alibaba.chaosblade.exec.plugin.zookeeper.ZookeeperConstant.*;

import com.alibaba.chaosblade.exec.common.aop.PointCut;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.ClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.NameClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.OrClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.MethodMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.NameMethodMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.OrMethodMatcher;

/** @author liuhq @Date 2020/11/23 上午11:36 */
public class ZookeeperPointCut implements PointCut {

  @Override
  public ClassMatcher getClassMatcher() {
    OrClassMatcher orClassMatcher = new OrClassMatcher();
    orClassMatcher.or(new NameClassMatcher(ZK_CLASS));
    return orClassMatcher;
  }

  @Override
  public MethodMatcher getMethodMatcher() {
    return new OrMethodMatcher()
        .or(new NameMethodMatcher(ZK_SEND_METHOD))
        .or(new NameMethodMatcher(ZK_SUBMIT_METHOD));
  }
}
