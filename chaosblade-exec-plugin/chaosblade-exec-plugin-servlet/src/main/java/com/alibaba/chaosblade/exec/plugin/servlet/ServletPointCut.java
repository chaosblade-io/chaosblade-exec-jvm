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

package com.alibaba.chaosblade.exec.plugin.servlet;

import com.alibaba.chaosblade.exec.common.aop.PointCut;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.ClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.NameClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.OrClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.SuperClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.AndMethodMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.ManyNameMethodMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.MethodMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.OrMethodMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.ParameterMethodMatcher;
import java.util.HashSet;
import java.util.Set;

/** @author Changjun Xiao */
public class ServletPointCut implements PointCut {

  public static final String SPRING_FRAMEWORK_SERVLET =
      "org.springframework.web.servlet.FrameworkServlet";
  public static final String ALIBABA_WEBX_FRAMEWORK_FILTER =
      "com.alibaba.citrus.webx.servlet.WebxFrameworkFilter";
  public static final String SPRING_HTTP_SERVLET_BEAN =
      "org.springframework.web.servlet.HttpServletBean";
  public static final String HTTP_SERVLET = "javax.servlet.http.HttpServlet";

  public static Set<String> enhanceMethodSet = new HashSet<String>();
  public static Set<String> enhanceMethodFilterSet = new HashSet<String>();

  static {
    enhanceMethodSet.add("doGet");
    enhanceMethodSet.add("doPost");
    enhanceMethodSet.add("doDelete");
    enhanceMethodSet.add("doPut");
    enhanceMethodFilterSet.add("doFilter");
  }

  @Override
  public ClassMatcher getClassMatcher() {
    OrClassMatcher orClassMatcher = new OrClassMatcher();
    orClassMatcher
        .or(new NameClassMatcher(SPRING_FRAMEWORK_SERVLET))
        .or(new NameClassMatcher(ALIBABA_WEBX_FRAMEWORK_FILTER))
        .or(new SuperClassMatcher(SPRING_HTTP_SERVLET_BEAN, HTTP_SERVLET));
    return orClassMatcher;
  }

  @Override
  public MethodMatcher getMethodMatcher() {
    AndMethodMatcher andMethodMatcher = new AndMethodMatcher();
    OrMethodMatcher orMethodMatcher = new OrMethodMatcher();
    orMethodMatcher
        .or(new ManyNameMethodMatcher(enhanceMethodSet))
        .or(new ManyNameMethodMatcher(enhanceMethodFilterSet));
    andMethodMatcher
        .and(orMethodMatcher)
        .and(new ParameterMethodMatcher(1, ParameterMethodMatcher.GREAT_THAN));
    return andMethodMatcher;
  }
}
