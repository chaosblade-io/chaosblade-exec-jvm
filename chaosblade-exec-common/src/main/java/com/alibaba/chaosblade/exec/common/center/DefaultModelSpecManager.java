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

import java.util.HashMap;
import java.util.Map;

import com.alibaba.chaosblade.exec.common.model.ModelSpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Changjun Xiao
 */
public class DefaultModelSpecManager implements ModelSpecManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultModelSpecManager.class);

    private Map<String, ModelSpec> modelSpecs = new HashMap<String, ModelSpec>();

    @Override
    public ModelSpec getModelSpec(String target) {
        return modelSpecs.get(target);
    }

    @Override
    public void registerModelSpec(ModelSpec modelSpec) {
        String target = modelSpec.getTarget();
        if (modelSpecs.containsKey(target)) {
            LOGGER.warn("{} target model has defined", target);
            return;
        }
        modelSpecs.put(target, modelSpec);
    }

    @Override
    public void load() {
    }

    @Override
    public void unload() {
        modelSpecs.clear();
    }
}
