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

package com.alibaba.chaosblade.exec.plugin.log.log4j2;

import com.alibaba.chaosblade.exec.common.aop.PointCut;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.*;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.MethodMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.NameMethodMatcher;

/** @author orion233 */
public class Log4j2PointCut implements PointCut {

  private static final String LOG4J_CONSOLE =
      "org.apache.logging.log4j.core.appender.rolling.RollingRandomAccessFileManager";
  private static final String LOG4J_METHOD_NAME = "writeToDestination";

  @Override
  public ClassMatcher getClassMatcher() {
    ClassMatcher log4j2AllClassMatcher = new NameClassMatcher(LOG4J_CONSOLE);
    return log4j2AllClassMatcher;
  }

  @Override
  public MethodMatcher getMethodMatcher() {
    return new NameMethodMatcher(LOG4J_METHOD_NAME);
  }
}
