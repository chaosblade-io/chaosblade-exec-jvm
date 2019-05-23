package com.alibaba.chaosblade.exec.plugin.http.model;

import com.alibaba.chaosblade.exec.common.model.matcher.BasePredicateMatcherSpec;

public class UriMatcherDefSpec extends BasePredicateMatcherSpec {

    @Override
    public String getName() {
        return "uri";
    }

    @Override
    public String getDesc() {
        return "url";
    }

    @Override
    public boolean noArgs() {
        return false;
    }

    @Override
    public boolean required() {
        return true;
    }
}
