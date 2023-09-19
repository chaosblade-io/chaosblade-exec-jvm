/*
 * Copyright 1999-2013 Alibaba Group Holding Ltd.
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

package com.alibaba.chaosblade.exec.plugin.hdfs.restful.webhdfs;

import com.alibaba.chaosblade.exec.common.aop.Enhancer;
import com.alibaba.chaosblade.exec.common.aop.PointCut;
import com.alibaba.chaosblade.exec.plugin.hdfs.common.HdfsConstant;
import com.alibaba.chaosblade.exec.plugin.hdfs.common.HdfsPlugin;

public class HdfsWebHdfsPlugin extends HdfsPlugin{
    @Override
    public String getName() {
        return HdfsConstant.PLUGIN_RESTFUL_WEBHDFS_NAME;
    }

    @Override
    public PointCut getPointCut() {
        return new HdfsWebHdfsPointCut();
    }

    @Override
    public Enhancer getEnhancer() {
        return new HdfsWebHdfsEnhancer();
    }
}
