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

import com.alibaba.chaosblade.exec.common.model.FlagSpec;

/**
 * @author Changjun Xiao
 */
public class ExceptionMessageFlagSpec implements FlagSpec {

    @Override
    public String getName() {
        return "exception-message";
    }

    @Override
    public String getDesc() {
        return "Specify exception message for exception experiment, default value is chaosblade-mock-exception";
    }

    @Override
    public boolean noArgs() {
        return false;
    }

    @Override
    public boolean required() {
        return false;
    }
}
