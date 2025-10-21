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

package com.alibaba.chaosblade.exec.plugin.gateway;

import com.alibaba.chaosblade.exec.common.aop.PointCut;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.ClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.NameClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.MethodMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.NameMethodMatcher;

/**
 * @Author wb-shd671576
 *
 * @package: com.alibaba.chaosblade.exec.plugin.gateway @Date 2021-07-29
 */
public class GatewayPointCut implements PointCut {

  private static final String GW_REST_CLASS =
      "org.springframework.cloud.gateway.handler.RoutePredicateHandlerMapping";
  private static final String GW_REST_METHOD = "getHandlerInternal";

  @Override
  public ClassMatcher getClassMatcher() {
    return new NameClassMatcher(GW_REST_CLASS);
  }

  @Override
  public MethodMatcher getMethodMatcher() {
    return new NameMethodMatcher(GW_REST_METHOD);
  }
}
