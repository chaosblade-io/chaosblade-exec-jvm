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

package com.alibaba.chaosblade.exec.plugin.postgrelsql;

import com.alibaba.chaosblade.exec.common.aop.PointCut;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.ClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.NameClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.MethodMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.NameMethodMatcher;

/** @author guoping.yao <a href="mailto:bryan880901@qq.com"> */
public class PostgrelsqlPointCut implements PointCut {

  private static final String POSTGELSQL_IO_CLASS = "org.postgresql.core.v3.QueryExecutorImpl";

  private static final String INTERCEPTOR_PRE_METHOD = "execute";

  @Override
  public ClassMatcher getClassMatcher() {
    return new NameClassMatcher(POSTGELSQL_IO_CLASS);
  }

  @Override
  public MethodMatcher getMethodMatcher() {
    return new NameMethodMatcher(INTERCEPTOR_PRE_METHOD);
  }
}
