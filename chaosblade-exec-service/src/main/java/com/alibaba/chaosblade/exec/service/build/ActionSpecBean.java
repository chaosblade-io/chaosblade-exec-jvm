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

package com.alibaba.chaosblade.exec.service.build;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.chaosblade.exec.common.model.FlagSpec;
import com.alibaba.chaosblade.exec.common.model.action.ActionSpec;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherSpec;

/**
 * @author Changjun Xiao
 */
public class ActionSpecBean {
    private String action;
    private String[] aliases;
    private String shortDesc;
    private String longDesc;
    private List<MatcherSpecBean> matchers;
    private List<FlagSpecBean> flags;

    public ActionSpecBean(ActionSpec spec) {
        this.action = spec.getName();
        this.aliases = spec.getAliases();
        this.shortDesc = spec.getShortDesc();
        this.longDesc = spec.getLongDesc();
        this.matchers = createMatchers(spec.getMatchers());
        this.flags = createFlags(spec.getActionFlags());
        // add jvm process flag to identify the experiment java process
        this.flags.add(new FlagSpecBean(new ProcessFlagBean()));
        this.flags.add(new FlagSpecBean(new ProcessIdBean()));
    }

    private List<FlagSpecBean> createFlags(List<FlagSpec> actionFlags) {
        ArrayList<FlagSpecBean> beans = new ArrayList<FlagSpecBean>();
        if (actionFlags == null) {
            return beans;
        }
        for (FlagSpec actionFlag : actionFlags) {
            beans.add(new FlagSpecBean(actionFlag));
        }
        return beans;
    }

    private List<MatcherSpecBean> createMatchers(List<MatcherSpec> matchers) {
        ArrayList<MatcherSpecBean> beans = new ArrayList<MatcherSpecBean>();
        if (matchers == null) {
            return beans;
        }
        for (MatcherSpec matcher : matchers) {
            beans.add(new MatcherSpecBean(matcher));
        }
        return beans;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String[] getAliases() {
        return aliases;
    }

    public void setAliases(String[] aliases) {
        this.aliases = aliases;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getLongDesc() {
        return longDesc;
    }

    public void setLongDesc(String longDesc) {
        this.longDesc = longDesc;
    }

    public List<MatcherSpecBean> getMatchers() {
        return matchers;
    }

    public void setMatchers(List<MatcherSpecBean> matchers) {
        this.matchers = matchers;
    }

    public List<FlagSpecBean> getFlags() {
        return flags;
    }

    public void setFlags(List<FlagSpecBean> flags) {
        this.flags = flags;
    }
}
