package com.alibaba.chaosblade.exec.plugin.security;

import com.alibaba.chaosblade.exec.common.model.matcher.BasePredicateMatcherSpec;

/**
 * @author liubin477@163.com
 */
public class SecurityMatcherSpec extends BasePredicateMatcherSpec {

    @Override
    public String getName() {
        return "username";
    }

    @Override
    public String getDesc() {
        return "SecurityMatcherSpec";
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
