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

package com.alibaba.chaosblade.exec.plugin.jedis;

import com.alibaba.chaosblade.exec.common.model.FrameworkModelSpec;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherSpec;

import java.util.ArrayList;
import java.util.List;

/**
 * @author guoping.yao <a href="mailto:bryan880901@qq.com">
 */
public class JedisModelSpec extends FrameworkModelSpec {

    @Override
    protected List<MatcherSpec> createNewMatcherSpecs() {
        ArrayList<MatcherSpec> matcherSpecs = new ArrayList<MatcherSpec>();
      //  matcherSpecs.add(new JedisHostMatcherSpec());
      //  matcherSpecs.add(new JedisPortMatcherSpec());
      //  matcherSpecs.add(new JedisDatabaseMatcherSpec());
        matcherSpecs.add(new JedisCmdTypeMatcherSpec());
        matcherSpecs.add(new JedisKeyMatcherSpec());
        return matcherSpecs;
    }

    @Override
    public String getTarget() {
        return JedisConstant.TARGET_NAME;
    }

    @Override
    public String getShortDesc() {
        return "jedis experiment";
    }

    @Override
    public String getLongDesc() {
        return "jedis experiment contains delay and exception by command and so on.";
    }

    @Override
    public String getExample() {
        return "jedis  --cmd hset --key test_key ";
    }
}
