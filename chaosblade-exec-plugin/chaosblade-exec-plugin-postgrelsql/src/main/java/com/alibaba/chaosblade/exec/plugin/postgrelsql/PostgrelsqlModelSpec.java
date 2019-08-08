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

package com.alibaba.chaosblade.exec.plugin.postgrelsql;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.chaosblade.exec.common.model.FrameworkModelSpec;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherSpec;

/**
 * @author guoping.yao <a href="mailto:bryan880901@qq.com">
 */
public class PostgrelsqlModelSpec extends FrameworkModelSpec {

    @Override
    protected List<MatcherSpec> createNewMatcherSpecs() {
        ArrayList<MatcherSpec> matcherSpecs = new ArrayList<MatcherSpec>();
        matcherSpecs.add(new PostgrelsqlHostMatcherSpec());
        matcherSpecs.add(new PostgrelsqlTableMatcherSpec());
        matcherSpecs.add(new PostgrelsqlDatabaseMatcherSpec());
        matcherSpecs.add(new PostgrelsqlSqlTypeMatcherSpec());
        matcherSpecs.add(new PostgrelsqlPortMatcherSpec());
        return matcherSpecs;
    }

    @Override
    public String getTarget() {
        return PostgrelsqlConstant.TARGET_NAME;
    }

    @Override
    public String getShortDesc() {
        return "postgresql experiment";
    }

    @Override
    public String getLongDesc() {
        return "postgresql experiment contains delay and exception by table name and so on.";
    }

    @Override
    public String getExample() {
        return "psql --sqltype select --port 5432";
    }
}
