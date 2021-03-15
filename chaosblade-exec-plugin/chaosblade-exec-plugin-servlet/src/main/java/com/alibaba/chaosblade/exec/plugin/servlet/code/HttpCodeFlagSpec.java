package com.alibaba.chaosblade.exec.plugin.servlet.code;

import com.alibaba.chaosblade.exec.common.model.FlagSpec;

/**
 * @author yefei
 */
public class HttpCodeFlagSpec implements FlagSpec {

    @Override
    public String getName() {
        return "code";
    }

    @Override
    public String getDesc() {
        return "http code";
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
