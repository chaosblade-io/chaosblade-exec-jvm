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

package com.alibaba.chaosblade.exec.common.model.action.returnv;

import java.lang.reflect.Method;

import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.common.exception.InterruptProcessException;

/**
 * @author Changjun Xiao
 */
public abstract class BaseReturnValueExecutor implements ReturnValueExecutor {
    private ValueFlagSpec valueFlagSpec;

    public BaseReturnValueExecutor(ValueFlagSpec valueFlagSpec) {
        this.valueFlagSpec = valueFlagSpec;
    }

    @Override
    public void run(EnhancerModel enhancerModel) throws Exception {
        // get return value from model action
        String value = enhancerModel.getActionFlag(valueFlagSpec.getName());
        Method method = enhancerModel.getMethod();
        if (method == null) {
            return;
        }
        Object returnValue = generateReturnValue(enhancerModel.getClassLoader(), method, value);
        InterruptProcessException.throwReturnImmediately(returnValue);
    }
}
