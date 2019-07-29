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

import java.lang.reflect.Method;

import com.alibaba.chaosblade.exec.common.model.action.ActionExecutor;

/**
 * @author Changjun Xiao
 */
public interface ThrowExceptionExecutor extends ActionExecutor {

    String THROW_CUSTOM_EXCEPTION = "throwCustomException";
    String THROW_DECLARED_EXCEPTION = "throwDeclaredException";

    /**
     * Throw custom exception
     *
     * @param classLoader
     * @param exception
     * @param exceptionMessage
     * @return
     */
    Exception throwCustomException(ClassLoader classLoader, String exception, String exceptionMessage);

    /**
     * Throw the first exception of the method declared
     *
     * @param classLoader
     * @param method
     * @param exceptionMessage
     * @return
     */
    Exception throwDeclaredException(ClassLoader classLoader, Method method, String exceptionMessage);
}
