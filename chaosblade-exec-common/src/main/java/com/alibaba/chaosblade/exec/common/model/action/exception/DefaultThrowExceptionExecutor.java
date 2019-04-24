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

/**
 * @author Changjun Xiao
 */
public class DefaultThrowExceptionExecutor implements ThrowExceptionExecutor {
    private FlagSpec exceptionFlag;

    public DefaultThrowExceptionExecutor() {
    }

    public DefaultThrowExceptionExecutor(FlagSpec exceptionFlag) {
        this.exceptionFlag = exceptionFlag;
    }

    @Override
    public void run(EnhancerModel enhancerModel) throws Exception {
        Exception exception = null;
        if (enhancerModel.getAction().equals(THROW_CUSTOM_EXCEPTION)) {
            exception = throwCustomException(enhancerModel.getClassLoader(), enhancerModel.getActionFlag(exceptionFlag
                .getName()));
        } else if (enhancerModel.getAction().equals(THROW_DECLARED_EXCEPTION)) {
            exception = throwDeclaredException(enhancerModel.getClassLoader(), enhancerModel.getMethod());
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
    public Exception throwCustomException(ClassLoader classLoader, String exception) {
        try {
            Class<?> clazz = classLoader.loadClass(exception);
            if (Exception.class.isAssignableFrom(clazz)) {
            	Constructor<?>[] constructors = clazz.getConstructors();
    			for(Constructor<?> constructor: constructors) {
    				if(constructor.getParameterCount() == 0) 
    					return (Exception)constructor.newInstance();
    				else if (constructor.getParameterCount() == 1 && constructor.getParameters()[0].getType().getSimpleName().equals("String")) 
    	                return (Exception)constructor.newInstance("chaosblade-mock-exception");
    			}
                return new RuntimeException("Failed to instantiate exception: "+ exception +", no default or single-string-param constructor found.");
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
    public Exception throwDeclaredException(ClassLoader classLoader, Method method) {
        Class<?>[] exceptionTypes = method.getExceptionTypes();
        if (exceptionTypes == null || exceptionTypes.length == 0) {
            return null;
        }
        Class<?> exceptionType = exceptionTypes[0];
        try {
            if (Exception.class.isAssignableFrom(exceptionType)) {
            	Constructor<?>[] constructors = exceptionType.getConstructors();
    			for(Constructor<?> constructor: constructors) {
    				if(constructor.getParameterCount() == 0) 
    					return (Exception)constructor.newInstance();
    				else if (constructor.getParameterCount() == 1 && constructor.getParameters()[0].getType().getSimpleName().equals("String")) 
    	                return (Exception)constructor.newInstance("chaosblade-mock-exception");
    			}
                return new RuntimeException("Failed to instantiate exception: "+ exceptionType.getName() +", no default or single-string-param constructor found.");
            }
            return new RuntimeException("the " + exceptionType.getName() + " not assign from java.lang.Exception");
        } catch (Throwable e) {
            return new RuntimeException("mock first declared exception for method error", e);
        }
    }
}
