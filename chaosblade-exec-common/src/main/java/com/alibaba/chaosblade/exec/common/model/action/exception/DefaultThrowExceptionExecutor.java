/*
 * Copyright 1999-2019 Alibaba Group Holding Ltd.
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

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.common.exception.InterruptProcessException;
import com.alibaba.chaosblade.exec.common.model.FlagSpec;
import com.alibaba.chaosblade.exec.common.util.StringUtil;

/**
 * @author Changjun Xiao
 */
public class DefaultThrowExceptionExecutor implements ThrowExceptionExecutor {
    private static final String DEFAULT_EXCEPTION_MESSAGE = "chaosblade-mock-exception";

    private FlagSpec exceptionFlag;
    private FlagSpec exceptionMessageFlag;

    public DefaultThrowExceptionExecutor() {
    }

    public DefaultThrowExceptionExecutor(FlagSpec exceptionFlag, FlagSpec exceptionMessageFlag) {
        this.exceptionFlag = exceptionFlag;
        this.exceptionMessageFlag = exceptionMessageFlag;
    }

    @Override
    public void run(EnhancerModel enhancerModel) throws Exception {
        Exception exception = null;
        String exceptionMessage = enhancerModel.getActionFlag(exceptionMessageFlag.getName());
        if (StringUtil.isBlank(exceptionMessage)) {
            exceptionMessage = DEFAULT_EXCEPTION_MESSAGE;
        }
        if (enhancerModel.getAction().equals(THROW_CUSTOM_EXCEPTION)) {
            exception = throwCustomException(enhancerModel.getClassLoader(), enhancerModel.getActionFlag(exceptionFlag
                .getName()), exceptionMessage);
        } else if (enhancerModel.getAction().equals(THROW_DECLARED_EXCEPTION)) {
            exception = throwDeclaredException(enhancerModel.getClassLoader(), enhancerModel.getMethod(),
                exceptionMessage);
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
    public Exception throwCustomException(ClassLoader classLoader, String exception, String exceptionMessage) {
        try {
            Class<?> clazz = classLoader.loadClass(exception);
            if (Exception.class.isAssignableFrom(clazz)) {
                Constructor<?>[] constructors = clazz.getConstructors();
                for (Constructor<?> constructor : constructors) {
                    if (constructor.getParameterTypes().length == 0) {
                        return (Exception)constructor.newInstance();
                    } else if (constructor.getParameterTypes().length == 1
                        && constructor.getParameterTypes()[0].getName().equals("java.lang.String")) {
                        return (Exception)constructor.newInstance(exceptionMessage);
                    }
                }
                return new RuntimeException("Failed to instantiate exception: " + exception
                    + ", no default or single-string-param constructor found.");
            }
            return new RuntimeException(exception + " not assign from java.lang.Exception");
        } catch (Throwable e) {
            return new RuntimeException(
                "mock custom exception: " + exception + " occurs error", e);
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
    public Exception throwDeclaredException(ClassLoader classLoader, Method method, String exceptionMessage) {
        Class<?>[] exceptionTypes = method.getExceptionTypes();
        if (exceptionTypes == null || exceptionTypes.length == 0) {
            return null;
        }
        Class<?> exceptionType = exceptionTypes[0];
        try {
            if (Exception.class.isAssignableFrom(exceptionType)) {
                Constructor<?>[] constructors = exceptionType.getConstructors();
                for (Constructor<?> constructor : constructors) {
                    if (constructor.getParameterTypes().length == 0) {
                        return (Exception)constructor.newInstance();
                    } else if (constructor.getParameterTypes().length == 1
                        && constructor.getParameterTypes()[0].getName().equals("java.lang.String")) {
                        return (Exception)constructor.newInstance(exceptionMessage);
                    }
                }
                return new RuntimeException("Failed to instantiate exception: " + exceptionType.getName()
                    + ", no default or single-string-param constructor found.");
            }
            return new RuntimeException("the " + exceptionType.getName() + " not assign from java.lang.Exception");
        } catch (Throwable e) {
            return new RuntimeException("mock first declared exception for method error", e);
        }
    }
}
