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

package com.alibaba.chaosblade.exec.plugin.http.httpclient5;

import com.alibaba.chaosblade.exec.common.aop.PointCut;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.ClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.NameClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.OrClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.AndMethodMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.MethodMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.NameMethodMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.OrMethodMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.ParameterMethodMatcher;

/** HttpClient5 injection point definition. */
public class HttpClient5PointCut implements PointCut {

  @Override
  public ClassMatcher getClassMatcher() {
    return new OrClassMatcher()
        .or(new NameClassMatcher("org.apache.hc.client5.http.impl.classic.CloseableHttpClient"))
        .or(new NameClassMatcher("org.apache.hc.client5.http.impl.classic.InternalHttpClient"))
        .or(new NameClassMatcher("org.apache.hc.client5.http.impl.classic.MinimalHttpClient"));
  }

  @Override
  public MethodMatcher getMethodMatcher() {
    AndMethodMatcher methodMatcher = new AndMethodMatcher();
    OrMethodMatcher orMethodMatcher = new OrMethodMatcher();
    orMethodMatcher.or(new NameMethodMatcher("execute")).or(new NameMethodMatcher("doExecute"));

    ParameterMethodMatcher parameterMethodMatcher =
        new ParameterMethodMatcher(
            new String[] {
              "org.apache.hc.core5.http.HttpHost",
              "org.apache.hc.core5.http.ClassicHttpRequest",
              "org.apache.hc.core5.http.protocol.HttpContext"
            },
            3,
            ParameterMethodMatcher.EQUAL);
    methodMatcher.and(orMethodMatcher).and(parameterMethodMatcher);
    return methodMatcher;
  }
}
