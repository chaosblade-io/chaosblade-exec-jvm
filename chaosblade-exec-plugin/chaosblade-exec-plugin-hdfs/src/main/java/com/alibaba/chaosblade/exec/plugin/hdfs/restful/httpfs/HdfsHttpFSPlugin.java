package com.alibaba.chaosblade.exec.plugin.hdfs.restful.httpfs;

import com.alibaba.chaosblade.exec.common.aop.Enhancer;
import com.alibaba.chaosblade.exec.common.aop.PointCut;
import com.alibaba.chaosblade.exec.plugin.hdfs.common.HdfsConstant;
import com.alibaba.chaosblade.exec.plugin.hdfs.common.HdfsPlugin;

public class HdfsHttpFSPlugin extends HdfsPlugin {
    @Override
    public String getName() {
        return HdfsConstant.PLUGIN_RESTFUL_HTTPFS_NAME;
    }

    @Override
    public PointCut getPointCut() {
        return new HdfsHttpFSPointCut();
    }

    @Override
    public Enhancer getEnhancer() {
        return new HdfsHttpFSEnhancer();
    }
}
