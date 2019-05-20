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

package com.alibaba.chaosblade.exec.common.plugin;

import com.alibaba.chaosblade.exec.common.aop.PredicateResult;
import com.alibaba.chaosblade.exec.common.constant.ModelConstant;
import com.alibaba.chaosblade.exec.common.model.BaseModelSpec;
import com.alibaba.chaosblade.exec.common.model.Model;
import com.alibaba.chaosblade.exec.common.model.action.delay.DelayActionSpec;
import com.alibaba.chaosblade.exec.common.model.action.exception.ThrowCustomExceptionActionSpec;
import com.alibaba.chaosblade.exec.common.model.action.exception.ThrowDeclaredExceptionActionSpec;
import com.alibaba.chaosblade.exec.common.model.action.returnv.ReturnValueActionSpec;

/**
 * @author Changjun Xiao
 */
public class MethodModelSpec extends BaseModelSpec {

    public MethodModelSpec() {
        addThrowExceptionActionDef();
        addReturnValueAction();
        addDelayAction();
        addMethodMatcherDef();
    }

    private void addDelayAction() {
        addActionSpec(new DelayActionSpec());
    }

    private void addReturnValueAction() {
        ReturnValueActionSpec returnValueActionSpec = new ReturnValueActionSpec();
        addActionSpec(returnValueActionSpec);
    }

    private void addMethodMatcherDef() {
        addMatcherDefToAllActions(new ClassNameMatcherSpec(false, true));
        addMatcherDefToAllActions(new MethodNameMatcherSpec(false, true));
    }

    private void addThrowExceptionActionDef() {
        ThrowCustomExceptionActionSpec throwCustomExceptionActionDef = new ThrowCustomExceptionActionSpec();
        ThrowDeclaredExceptionActionSpec throwDeclaredExceptionActionDef = new ThrowDeclaredExceptionActionSpec();
        addActionSpec(throwCustomExceptionActionDef);
        addActionSpec(throwDeclaredExceptionActionDef);
    }

    @Override
    protected PredicateResult preMatcherPredicate(Model matcherSpecs) {
        return PredicateResult.success();
    }

    @Override
    public String getTarget() {
        return ModelConstant.JVM_TARGET;
    }

    @Override
    public String getShortDesc() {
        return "method";
    }

    @Override
    public String getLongDesc() {
        return "method";
    }

    @Override
    public String getExample() {
        return "method";
    }
}
