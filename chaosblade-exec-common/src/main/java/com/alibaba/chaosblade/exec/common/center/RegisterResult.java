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

package com.alibaba.chaosblade.exec.common.center;

import com.alibaba.chaosblade.exec.common.model.Model;

/**
 * @author Changjun Xiao
 */
public class RegisterResult {
    private Model model;
    private boolean forceSuccess;
    private boolean createSuccess;

    public RegisterResult(Model model, boolean forceSuccess, boolean createSuccess) {
        this.model = model;
        this.forceSuccess = forceSuccess;
        this.createSuccess = createSuccess;
    }

    public static RegisterResult forceSuccess() {
        return new RegisterResult(null, true,false);
    }
    public static RegisterResult createSuccess() {
        return new RegisterResult(null, false,true);
    }

    public static RegisterResult fail(Model model) {
        return new RegisterResult(model, false,false);
    }


    public Model getModel() {
        return model;
    }

    public boolean isForceSuccess() {
        return forceSuccess;
    }

    public boolean isCreateSuccess() {
        return createSuccess;
    }
}
