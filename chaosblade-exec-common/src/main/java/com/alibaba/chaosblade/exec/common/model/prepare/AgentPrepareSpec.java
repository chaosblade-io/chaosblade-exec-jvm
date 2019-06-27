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

package com.alibaba.chaosblade.exec.common.model.prepare;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.chaosblade.exec.common.model.FlagSpec;

/**
 * @author Changjun Xiao
 */
public class AgentPrepareSpec implements PrepareSpec {

    @Override
    public String getType() {
        return "jvm";
    }

    @Override
    public List<FlagSpec> getFlags() {
        ArrayList<FlagSpec> flagSpecs = new ArrayList<FlagSpec>();
        FlagSpec flagSpec = new FlagSpec() {
            @Override
            public String getName() {
                return "process";
            }

            @Override
            public String getDesc() {
                return "java application process";
            }

            @Override
            public boolean noArgs() {
                return false;
            }

            @Override
            public boolean required() {
                return true;
            }
        };
        flagSpecs.add(flagSpec);
        FlagSpec javaHome = new FlagSpec() {
            @Override
            public String getName() {
                return "javaHome";
            }

            @Override
            public String getDesc() {
                return "java home path";
            }

            @Override
            public boolean noArgs() {
                return false;
            }

            @Override
            public boolean required() {
                return false;
            }
        };
        flagSpecs.add(javaHome);
        return flagSpecs;
    }

    @Override
    public boolean required() {
        return true;
    }
}
