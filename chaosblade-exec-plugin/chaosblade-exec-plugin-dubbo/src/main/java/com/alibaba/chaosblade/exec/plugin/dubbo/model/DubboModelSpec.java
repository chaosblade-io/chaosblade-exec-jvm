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

package com.alibaba.chaosblade.exec.plugin.dubbo.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.alibaba.chaosblade.exec.common.aop.PredicateResult;
import com.alibaba.chaosblade.exec.common.model.FrameworkModelSpec;
import com.alibaba.chaosblade.exec.common.model.Model;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherModel;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherSpec;
import com.alibaba.chaosblade.exec.plugin.dubbo.DubboConstant;

/**
 * @author Changjun Xiao
 */
public class DubboModelSpec extends FrameworkModelSpec {

    @Override
    public String getShortDesc() {
        return "dubbo experiment";
    }

    @Override
    public String getLongDesc() {
        return "Dubbo experiment for testing service delay and exception.";
    }

    @Override
    public String getExample() {
        return "dubbo delay --time 3000 --consumer --service com.example.service.HellService";
    }

    @Override
    protected List<MatcherSpec> createNewMatcherSpecs() {
        ArrayList<MatcherSpec> matcherSpecs = new ArrayList<MatcherSpec>();
        matcherSpecs.add(new ConsumerMatcherSpec());
        matcherSpecs.add(new ProviderMatcherSpec());
        matcherSpecs.add(new AppNameMatcherDefSpec());
        matcherSpecs.add(new ServiceMatcherSpec());
        matcherSpecs.add(new VersionMatcherSpec());
        matcherSpecs.add(new MethodMatcherSpec());
        return matcherSpecs;
    }

    @Override
    protected PredicateResult preMatcherPredicate(Model model) {
        if (model == null) {
            return PredicateResult.fail("matcher not found for dubbo");
        }
        MatcherModel matcher = model.getMatcher();
        Set<String> keySet = matcher.getMatchers().keySet();
        for (String key : keySet) {
            if (key.equals(DubboConstant.CONSUMER_KEY) || key.equals(DubboConstant.PROVIDER_KEY)) {
                return PredicateResult.success();
            }
        }
        return PredicateResult.fail("less necessary matcher is consumer or provider for dubbo");
    }

    @Override
    public String getTarget() {
        return DubboConstant.TARGET_NAME;
    }

}
