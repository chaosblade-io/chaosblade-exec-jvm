package com.alibaba.chaosblade.exec.plugin.lettuce;

import com.alibaba.chaosblade.exec.common.model.FlagSpec;

import static com.alibaba.chaosblade.exec.plugin.lettuce.LettuceConstants.VALUE;

/**
 * @author yefei
 */
public class LettuceValueFlagSpec implements FlagSpec {

    @Override
    public String getName() {
        return VALUE;
    }

    @Override
    public String getDesc() {
        return "value set";
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