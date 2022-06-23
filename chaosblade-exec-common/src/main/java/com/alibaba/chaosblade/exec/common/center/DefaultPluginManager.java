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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.alibaba.chaosblade.exec.common.aop.PluginBean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author timge
 */
public class DefaultPluginManager implements PluginManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultModelSpecManager.class);

    private Map<String, Set<PluginBean>> pluginBeans = new HashMap<String, Set<PluginBean>>();

    @Override
    public void load() {

    }

    @Override
    public void unload() {

    }

    @Override
    public void registerPlugins(PluginBean pluginBean) {
        String target = pluginBean.getModelSpec().getTarget();
        if (pluginBeans.get(target) == null) {
            Set<PluginBean> pluginBeanSet = new HashSet<PluginBean>();
            pluginBeanSet.add(pluginBean);
            pluginBeans.put(target, pluginBeanSet);
        }
        pluginBeans.get(target).add(pluginBean);
    }

    @Override
    public Set<PluginBean> getPlugins(String target) {
        return pluginBeans.get(target);
    }
}
