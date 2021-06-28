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

import java.util.Arrays;
import java.util.List;

import com.alibaba.chaosblade.exec.common.aop.PredicateResult;
import com.alibaba.chaosblade.exec.common.constant.CategoryConstants;
import com.alibaba.chaosblade.exec.common.model.FlagSpec;
import com.alibaba.chaosblade.exec.common.model.action.ActionModel;
import com.alibaba.chaosblade.exec.common.model.action.BaseActionSpec;
import com.alibaba.chaosblade.exec.common.util.StringUtil;
import com.alibaba.chaosblade.exec.common.util.StringUtils;

/**
 * @author Changjun Xiao
 */
public class ThrowCustomExceptionActionSpec extends BaseActionSpec {

    private static FlagSpec exceptionFlag = new ExceptionFlagSpec();
    private static FlagSpec exceptionMessageFlag = new ExceptionMessageFlagSpec();

    public ThrowCustomExceptionActionSpec() {
        super(new DefaultThrowExceptionExecutor(exceptionFlag, exceptionMessageFlag));
    }

    @Override
    public String getName() {
        return ThrowExceptionExecutor.THROW_CUSTOM_EXCEPTION;
    }

    @Override
    public String[] getAliases() {
        return new String[] {"tce"};
    }

    @Override
    public String getShortDesc() {
        return "throw custom exception";
    }

    @Override
    public String getLongDesc() {
        if (StringUtils.isNotBlank(super.getLongDesc())) {
            return super.getLongDesc();
        }
        return "Throw custom exception with --exception option";
    }

    @Override
    public List<FlagSpec> getActionFlags() {
        return Arrays.asList(exceptionFlag, exceptionMessageFlag);
    }

    @Override
    public PredicateResult predicate(ActionModel actionModel) {
        String exceptionClass = actionModel.getFlag(exceptionFlag.getName());
        if (StringUtil.isBlank(exceptionClass)) {
            return PredicateResult.fail("less exception argument");
        }
        if (exceptionClass.startsWith("-")) {
            return PredicateResult.fail("illegal exception value");
        }
        return PredicateResult.success();
    }

    @Override
    public String[] getCategories() {
        return new String[] {CategoryConstants.JAVA_CUSTOM};
    }
}
