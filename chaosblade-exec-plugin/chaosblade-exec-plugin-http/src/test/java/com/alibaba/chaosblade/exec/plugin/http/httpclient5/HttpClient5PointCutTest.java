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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.alibaba.chaosblade.exec.common.aop.matcher.ClassInfo;
import com.alibaba.chaosblade.exec.common.aop.matcher.MethodInfo;
import org.junit.Test;

/** Tests for HttpClient5 pointcut. */
public class HttpClient5PointCutTest {

  private final HttpClient5PointCut pointCut = new HttpClient5PointCut();

  @Test
  public void shouldMatchSupportedClasses() {
    ClassInfo classInfo =
        new ClassInfo(
            0,
            null,
            null,
            new String[0],
            new String[0],
            Thread.currentThread().getContextClassLoader());

    assertTrue(
        pointCut
            .getClassMatcher()
            .isMatched("org.apache.hc.client5.http.impl.classic.CloseableHttpClient", classInfo));
    assertTrue(
        pointCut
            .getClassMatcher()
            .isMatched("org.apache.hc.client5.http.impl.classic.InternalHttpClient", classInfo));
    assertTrue(
        pointCut
            .getClassMatcher()
            .isMatched("org.apache.hc.client5.http.impl.classic.MinimalHttpClient", classInfo));
    assertFalse(
        pointCut
            .getClassMatcher()
            .isMatched("org.apache.http.impl.client.InternalHttpClient", classInfo));
  }

  @Test
  public void shouldMatchExecuteAndDoExecuteMethods() {
    String[] parameterTypes =
        new String[] {
          "org.apache.hc.core5.http.HttpHost",
          "org.apache.hc.core5.http.ClassicHttpRequest",
          "org.apache.hc.core5.http.protocol.HttpContext"
        };
    MethodInfo executeMethod =
        new MethodInfo(0, "execute", parameterTypes, new String[0], new String[0], null);
    MethodInfo doExecuteMethod =
        new MethodInfo(0, "doExecute", parameterTypes, new String[0], new String[0], null);
    MethodInfo wrongMethod =
        new MethodInfo(
            0,
            "execute",
            new String[] {"org.apache.hc.core5.http.ClassicHttpRequest"},
            new String[0],
            new String[0],
            null);

    assertTrue(pointCut.getMethodMatcher().isMatched("execute", executeMethod));
    assertTrue(pointCut.getMethodMatcher().isMatched("doExecute", doExecuteMethod));
    assertFalse(pointCut.getMethodMatcher().isMatched("execute", wrongMethod));
  }
}
