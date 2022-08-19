package com.alibaba.chaosblade.exec.common.model.matcher;

/**
 * Author: xiaochangjun
 */
public class SimplePredicateMatcherSpec extends BasePredicateMatcherSpec {
    private String name;
    private String desc;

    public SimplePredicateMatcherSpec(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDesc() {
        return desc;
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
