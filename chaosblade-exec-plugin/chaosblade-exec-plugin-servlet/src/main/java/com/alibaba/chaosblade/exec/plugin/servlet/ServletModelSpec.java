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

package com.alibaba.chaosblade.exec.plugin.servlet;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.chaosblade.exec.common.model.FrameworkModelSpec;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherSpec;

/**
 * @author Changjun Xiao
 */
public class ServletModelSpec extends FrameworkModelSpec {

    @Override
    public String getTarget() {
        return ServletConstant.TARGET_NAME;
    }

    @Override
    public String getShortDesc() {
        return "java servlet experiment";
    }

    @Override
    public String getLongDesc() {
        return "Java servlet experiment, support path, query string, servlet path and request method matcher";
    }

    @Override
    public String getExample() {
        return "servlet --pathinfo hello --method post";
    }

    @Override
    protected List<MatcherSpec> createNewMatcherSpecs() {
        ArrayList<MatcherSpec> matcherSpecs = new ArrayList<MatcherSpec>();
        matcherSpecs.add(new ServletPathInfoMatcherSpec());
        matcherSpecs.add(new ServletQueryStringMatcherSpec());
        matcherSpecs.add(new ServletPathMatcherSpec());
        matcherSpecs.add(new ServletMethodMatcherSpec());
        matcherSpecs.add(new ServletRequestPathMatcherSpec());
        return matcherSpecs;
    }
}
