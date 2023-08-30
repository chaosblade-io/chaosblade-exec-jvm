package com.alibaba.chaosblade.exec.plugin.hdfs.common.flag;

import com.alibaba.chaosblade.exec.common.model.matcher.BasePredicateMatcherSpec;
import com.alibaba.chaosblade.exec.plugin.hdfs.common.HdfsConstant;

public class HdfsFileMatcherSpec extends BasePredicateMatcherSpec {
    @Override
    public String getName() {
        return HdfsConstant.FLAG_COMMON_FILE;
    }

    @Override
    public String getDesc() {
        return "The target HDFS file path.";
    }

    @Override
    public boolean noArgs() {
        return false;
    }

    @Override
    public boolean required() {
        return false;
    }
}
