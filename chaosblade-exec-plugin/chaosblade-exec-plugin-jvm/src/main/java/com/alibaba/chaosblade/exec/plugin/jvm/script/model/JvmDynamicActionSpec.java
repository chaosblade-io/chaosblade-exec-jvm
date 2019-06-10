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

package com.alibaba.chaosblade.exec.plugin.jvm.script.model;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.chaosblade.exec.common.aop.PredicateResult;
import com.alibaba.chaosblade.exec.common.model.FlagSpec;
import com.alibaba.chaosblade.exec.common.model.action.ActionModel;
import com.alibaba.chaosblade.exec.common.model.action.BaseActionSpec;
import com.alibaba.chaosblade.exec.common.plugin.ClassNameMatcherSpec;
import com.alibaba.chaosblade.exec.common.plugin.MethodNameMatcherSpec;
import com.alibaba.chaosblade.exec.plugin.jvm.JvmConstant;

/**
 * @author Changjun Xiao
 */
public class JvmDynamicActionSpec extends BaseActionSpec {

    public JvmDynamicActionSpec() {
        super(new DynamicScriptExecutor());
        addMatcherDesc(new ClassNameMatcherSpec());
        addMatcherDesc(new MethodNameMatcherSpec(true));
    }

    @Override
    public String getName() {
        return JvmConstant.ACTION_DYNAMIC_SCRIPT_NAME;
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public String getShortDesc() {
        return "Dynamically execute custom scripts";
    }

    @Override
    public String getLongDesc() {
        return "Dynamically execute custom scripts";
    }

    @Override
    public List<FlagSpec> getActionFlags() {
        ArrayList<FlagSpec> flagSpecs = new ArrayList<FlagSpec>();
        flagSpecs.add(new ScriptFileFlagSpec());
        flagSpecs.add(new ScriptTypeFlagSpec());
        flagSpecs.add(new ScriptContentFlagSpec());
        flagSpecs.add(new ScriptNameFlagSpec());
        return flagSpecs;
    }

    @Override
    public PredicateResult predicate(ActionModel actionModel) {
        return PredicateResult.success();
    }
}
