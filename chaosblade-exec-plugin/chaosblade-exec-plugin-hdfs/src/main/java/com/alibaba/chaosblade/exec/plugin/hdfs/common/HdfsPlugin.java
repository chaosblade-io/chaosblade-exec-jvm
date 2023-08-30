package com.alibaba.chaosblade.exec.plugin.hdfs.common;

import com.alibaba.chaosblade.exec.common.aop.Plugin;
import com.alibaba.chaosblade.exec.common.model.ModelSpec;

public abstract class HdfsPlugin implements Plugin {
    @Override
    public ModelSpec getModelSpec() {
        return new HdfsModelSpec();
    }
}
