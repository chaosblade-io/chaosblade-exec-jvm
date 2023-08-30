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
