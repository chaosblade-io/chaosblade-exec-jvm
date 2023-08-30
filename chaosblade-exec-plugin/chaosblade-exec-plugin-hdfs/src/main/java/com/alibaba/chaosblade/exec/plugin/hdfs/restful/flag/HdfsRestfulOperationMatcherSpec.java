package com.alibaba.chaosblade.exec.plugin.hdfs.restful.flag;

import com.alibaba.chaosblade.exec.common.model.matcher.BasePredicateMatcherSpec;
import com.alibaba.chaosblade.exec.plugin.hdfs.common.HdfsConstant;

public class HdfsRestfulOperationMatcherSpec extends BasePredicateMatcherSpec {
    @Override
    public String getName() {
        return HdfsConstant.FLAG_RESTFUL_OPERATION;
    }

    @Override
    public String getDesc() {
        return "Restful operation executed on remote HDFS file.";
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
