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

package com.alibaba.chaosblade.exec.common.model.matcher;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Changjun Xiao
 */
public class MatcherModel {

    private Map<String, String> matchers;

    public MatcherModel() {
        this.matchers = new LinkedHashMap<String, String>();
    }

    public MatcherModel(Map<String, String> matchers) {
        this.matchers = matchers;
    }

    public void add(String name, String value) {
        this.matchers.put(name, value);
    }

    public String get(String name) {
        return matchers.get(name);
    }

    public Map<String, String> getMatchers() {
        return matchers;
    }
}
