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

package com.alibaba.chaosblade.exec.common.model.action.exception;

import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.common.exception.InterruptProcessException;
import com.alibaba.chaosblade.exec.common.model.FlagSpec;
import com.alibaba.chaosblade.exec.common.util.StringUtil;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/** @author Changjun Xiao */
public class DefaultThrowExceptionExecutor implements ThrowExceptionExecutor {
  private static final String DEFAULT_EXCEPTION_MESSAGE = "chaosblade-mock-exception";

  private FlagSpec exceptionFlag;
  private FlagSpec exceptionMessageFlag;

  public DefaultThrowExceptionExecutor() {}

  public DefaultThrowExceptionExecutor(FlagSpec exceptionFlag, FlagSpec exceptionMessageFlag) {
    this.exceptionFlag = exceptionFlag;
    this.exceptionMessageFlag = exceptionMessageFlag;
  }

  @Override
  public void run(EnhancerModel enhancerModel) throws Exception {
    Exception exception = null;
    String exceptionMessage = null;
    if (exceptionMessageFlag != null) {
      exceptionMessage = enhancerModel.getActionFlag(exceptionMessageFlag.getName());
    }
    if (StringUtil.isBlank(exceptionMessage)) {
      exceptionMessage = DEFAULT_EXCEPTION_MESSAGE;
    }
    if (enhancerModel.getAction().equals(THROW_CUSTOM_EXCEPTION)) {
      exception =
          throwCustomException(
              enhancerModel.getClassLoader(),
              enhancerModel.getActionFlag(exceptionFlag.getName()),
              exceptionMessage);
    } else if (enhancerModel.getAction().equals(THROW_DECLARED_EXCEPTION)) {
      exception =
          throwDeclaredException(
              enhancerModel.getClassLoader(), enhancerModel.getMethod(), exceptionMessage);
    }
    if (exception != null) {
      InterruptProcessException.throwThrowsImmediately(exception);
    }
  }

  /**
   * Throw custom exception
   *
   * @param classLoader
   * @param exception
   * @return
   */
  @Override
  public Exception throwCustomException(
      ClassLoader classLoader, String exception, String exceptionMessage) {
    try {
      Class<?> clazz = classLoader.loadClass(exception);
      return instantiateException(clazz, exceptionMessage);
    } catch (Throwable e) {
      return new RuntimeException("mock custom exception: " + exception + " occurs error", e);
    }
  }

  /**
   * Throw the first exception which is the method declared
   *
   * @param classLoader
   * @param method
   * @return
   */
  @Override
  public Exception throwDeclaredException(
      ClassLoader classLoader, Method method, String exceptionMessage) {
    Class<?>[] exceptionTypes = method.getExceptionTypes();
    if (exceptionTypes == null || exceptionTypes.length == 0) {
      return null;
    }
    Class<?> exceptionType = exceptionTypes[0];
    try {
      return instantiateException(exceptionType, exceptionMessage);
    } catch (Throwable e) {
      return new RuntimeException("mock first declared exception for method error", e);
    }
  }

  /**
   * instantiate exception with special class and message
   *
   * @param exceptionClass
   * @param exceptionMessage
   * @return
   * @throws IllegalAccessException
   * @throws InvocationTargetException
   * @throws InstantiationException
   */
  private Exception instantiateException(Class exceptionClass, String exceptionMessage)
      throws IllegalAccessException, InvocationTargetException, InstantiationException {
    if (Exception.class.isAssignableFrom(exceptionClass)) {
      Constructor<?>[] constructors = exceptionClass.getConstructors();
      // cache default constructor, if any
      Constructor constructorNoArgs = null;
      for (Constructor<?> constructor : constructors) {
        if (constructor.getParameterTypes().length == 0) {
          constructorNoArgs = constructor;
        } else if (constructor.getParameterTypes().length == 1
            && "java.lang.String".equals(constructor.getParameterTypes()[0].getName())) {
          return (Exception) constructor.newInstance(exceptionMessage);
        }
      }
      if (null != constructorNoArgs) {
        return (Exception) constructorNoArgs.newInstance();
      }
      return new RuntimeException(
          "Failed to instantiate exception: "
              + exceptionClass.getName()
              + ", no default or single-string-param constructor found.");
    }
    return new RuntimeException(
        "the " + exceptionClass.getName() + " not assign from java.lang.Exception");
  }
}
