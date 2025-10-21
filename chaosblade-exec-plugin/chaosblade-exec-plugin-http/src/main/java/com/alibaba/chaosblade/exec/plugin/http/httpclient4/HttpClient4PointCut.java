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

package com.alibaba.chaosblade.exec.plugin.http.httpclient4;

import com.alibaba.chaosblade.exec.common.aop.PointCut;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.ClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.NameClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.OrClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.AndMethodMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.MethodMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.NameMethodMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.OrMethodMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.ParameterMethodMatcher;

/**
 * @Author yuhan
 *
 * @package: com.alibaba.chaosblade.exec.plugin.http.httpclient4 @Date 2019-05-22 16:10
 */
public class HttpClient4PointCut implements PointCut {

  @Override
  public ClassMatcher getClassMatcher() {
    OrClassMatcher classMatcher = new OrClassMatcher();
    classMatcher
        .or(new NameClassMatcher("org.apache.http.impl.client.AbstractHttpClient"))
        .or(new NameClassMatcher("org.apache.http.impl.client.MinimalHttpClient"))
        .or(new NameClassMatcher("org.apache.http.impl.client.InternalHttpClient"));
    return classMatcher;
  }

  @Override
  public MethodMatcher getMethodMatcher() {
    AndMethodMatcher methodMatcher = new AndMethodMatcher();
    OrMethodMatcher orMethodMatcher = new OrMethodMatcher();
    orMethodMatcher.or(new NameMethodMatcher("execute")).or(new NameMethodMatcher("doExecute"));

    ParameterMethodMatcher parameterMethodMatcher =
        new ParameterMethodMatcher(
            new String[] {
              "org.apache.http.HttpHost",
              "org.apache.http.HttpRequest",
              "org.apache.http.protocol.HttpContext"
            },
            3,
            ParameterMethodMatcher.EQUAL);
    methodMatcher.and(orMethodMatcher).and(parameterMethodMatcher);
    return methodMatcher;
  }
}
