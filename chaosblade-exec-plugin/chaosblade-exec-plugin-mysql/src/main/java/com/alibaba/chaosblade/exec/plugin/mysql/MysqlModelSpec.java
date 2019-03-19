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

package com.alibaba.chaosblade.exec.plugin.mysql;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.chaosblade.exec.common.model.FrameworkModelSpec;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherSpec;

/**
 * @author Changjun Xiao
 */
public class MysqlModelSpec extends FrameworkModelSpec {

    @Override
    protected List<MatcherSpec> createNewMatcherSpecs() {
        ArrayList<MatcherSpec> matcherSpecs = new ArrayList<MatcherSpec>();
        matcherSpecs.add(new MysqlHostMatcherSpec());
        matcherSpecs.add(new MysqlTableMatcherSpec());
        matcherSpecs.add(new MysqlDatabaseMatcherSpec());
        matcherSpecs.add(new MysqlSqlTypeMatcherSpec());
        matcherSpecs.add(new MysqlPortMatcherSpec());
        return matcherSpecs;
    }

    @Override
    public String getTarget() {
        return MysqlConstant.TARGET_NAME;
    }

    @Override
    public String getShortDesc() {
        return "mysql experiment";
    }

    @Override
    public String getLongDesc() {
        return "Mysql experiment contains delay and exception by table name and so on.";
    }

    @Override
    public String getExample() {
        return "mysql --sqltype select --port 3306";
    }
}
