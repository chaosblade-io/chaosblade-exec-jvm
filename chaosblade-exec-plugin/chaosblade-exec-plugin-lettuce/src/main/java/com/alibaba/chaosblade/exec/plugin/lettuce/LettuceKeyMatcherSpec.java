package com.alibaba.chaosblade.exec.plugin.lettuce;

import com.alibaba.chaosblade.exec.common.model.matcher.BasePredicateMatcherSpec;

import static com.alibaba.chaosblade.exec.plugin.lettuce.LettuceConstants.KEY;

/**
 * @author yefei
 */
public class LettuceKeyMatcherSpec extends BasePredicateMatcherSpec {

    @Override
    public String getName() {
        return KEY;
    }

    @Override
    public String getDesc() {
        return "key matcher";
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