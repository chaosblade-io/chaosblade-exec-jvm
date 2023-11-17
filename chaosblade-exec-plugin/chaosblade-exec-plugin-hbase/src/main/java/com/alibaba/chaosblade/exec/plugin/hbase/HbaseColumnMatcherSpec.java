package com.alibaba.chaosblade.exec.plugin.hbase;

import com.alibaba.chaosblade.exec.common.model.matcher.BasePredicateMatcherSpec;

public class HbaseColumnMatcherSpec extends BasePredicateMatcherSpec{
    @Override
    public String getName() {
        return HbaseConstant.Column;
    }

    @Override
    public String getDesc() {
        return "The hbase column which used";
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
