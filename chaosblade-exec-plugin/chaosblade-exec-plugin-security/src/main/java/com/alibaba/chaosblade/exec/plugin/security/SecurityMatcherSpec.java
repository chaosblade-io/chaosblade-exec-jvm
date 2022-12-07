package com.alibaba.chaosblade.exec.plugin.security;

import com.alibaba.chaosblade.exec.common.model.matcher.BasePredicateMatcherSpec;

/**
 * @author liubin@njzfit.cn
 */
public class SecurityMatcherSpec extends BasePredicateMatcherSpec {

    @Override
    public String getName() {
        return SecurityConstant.PARAM_USERNAME;
    }

    @Override
    public String getDesc() {
        return "login username";
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
