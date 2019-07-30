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

package com.alibaba.chaosblade.exec.service.handler;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.alibaba.chaosblade.exec.common.model.FlagSpec;
import com.alibaba.chaosblade.exec.common.model.Model;
import com.alibaba.chaosblade.exec.common.model.action.ActionSpec;
import com.alibaba.chaosblade.exec.common.transport.Request;
import com.alibaba.chaosblade.exec.common.util.StringUtil;

/**
 * @author Changjun Xiao
 */
public class ModelParser {

    public final static Set<String> assistantFlag = new HashSet<String>(Arrays.asList(
        "target", "action", "process", "pid", "debug", "suid", "help", "pod"
    ));

    public static Model parseRequest(String target, Request request, ActionSpec actionSpec) {
        Model model = new Model(target, actionSpec.getName());
        Map<String, String> params = request.getParams();
        List<FlagSpec> actionFlags = actionSpec.getActionFlags();
        if (actionFlags != null) {
            for (FlagSpec actionFlag : actionFlags) {
                String flagValue = request.getParam(actionFlag.getName());
                if (StringUtil.isBlank(flagValue)) {
                    continue;
                }
                model.getAction().addFlag(actionFlag.getName(), flagValue);
                // delete itnot assign from
                request.getParams().remove(actionFlag.getName());
            }
        }
        for (Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();
            if (assistantFlag.contains(key)) {
                continue;
            }
            // add matcher flag
            model.getMatcher().add(key, entry.getValue());
        }
        return model;
    }
}
