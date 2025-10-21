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

package com.alibaba.chaosblade.exec.bootstrap.jvmsandbox;

import com.alibaba.chaosblade.exec.common.aop.Plugin;
import com.alibaba.chaosblade.exec.common.aop.PointCut;
import com.alibaba.chaosblade.exec.common.aop.matcher.ClassInfo;
import com.alibaba.chaosblade.exec.common.aop.matcher.MethodInfo;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.ClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.MethodMatcher;
import com.alibaba.chaosblade.exec.common.center.ManagerFactory;
import com.alibaba.jvm.sandbox.api.filter.Filter;
import com.alibaba.jvm.sandbox.api.listener.EventListener;

/** @author Changjun Xiao */
public class SandboxEnhancerFactory {

  public static final String JAVA = "java.";
  public static final String SUN = "sun.";
  public static final String CHAOSBLADE = "com.alibaba.chaosblade";

  /**
   * Create class and method filter to match the pointcut
   *
   * @param enhancerClassName
   * @param pointCut
   * @return
   */
  public static Filter createFilter(final String enhancerClassName, final PointCut pointCut) {
    return new Filter() {
      @Override
      public boolean doClassFilter(
          int access,
          String javaClassName,
          String superClassTypeJavaClassName,
          String[] interfaceTypeJavaClassNameArray,
          String[] annotationTypeJavaClassNameArray) {
        if (javaClassName.startsWith(JAVA)
            || javaClassName.startsWith(SUN)
            || javaClassName.startsWith("[")
            || javaClassName.startsWith(CHAOSBLADE)) {
          return false;
        }
        if (pointCut == null) {
          return false;
        }
        ClassMatcher classMatcher = pointCut.getClassMatcher();
        if (classMatcher == null) {
          return false;
        }
        return classMatcher.isMatched(
            javaClassName,
            new ClassInfo(
                access,
                javaClassName,
                superClassTypeJavaClassName,
                interfaceTypeJavaClassNameArray,
                annotationTypeJavaClassNameArray,
                null));
      }

      @Override
      public boolean doMethodFilter(
          int access,
          String javaMethodName,
          String[] parameterTypeJavaClassNameArray,
          String[] throwsTypeJavaClassNameArray,
          String[] annotationTypeJavaClassNameArray) {
        MethodMatcher methodMatcher = pointCut.getMethodMatcher();
        if (methodMatcher == null) {
          return false;
        }
        boolean match =
            methodMatcher.isMatched(
                javaMethodName,
                new MethodInfo(
                    access,
                    javaMethodName,
                    parameterTypeJavaClassNameArray,
                    throwsTypeJavaClassNameArray,
                    annotationTypeJavaClassNameArray,
                    null));
        if (match) {
          ManagerFactory.getStatusManager().registerEnhancer(enhancerClassName);
        }
        return match;
      }
    };
  }

  /**
   * Create the before event listener for handing the method enhancer
   *
   * @param plugin
   * @return
   */
  public static EventListener createBeforeEventListener(Plugin plugin) {
    return new BeforeEventListener(plugin);
  }

  /**
   * Create the after event listener for handing the method result
   *
   * @param plugin
   * @return
   */
  public static EventListener createAfterEventListener(Plugin plugin) {
    return new AfterEventListener(plugin);
  }
}
