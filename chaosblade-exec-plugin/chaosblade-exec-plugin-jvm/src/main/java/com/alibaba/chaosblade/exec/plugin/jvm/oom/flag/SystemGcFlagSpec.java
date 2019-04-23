package com.alibaba.chaosblade.exec.plugin.jvm.oom.flag;

import com.alibaba.chaosblade.exec.common.model.FlagSpec;
import com.alibaba.chaosblade.exec.plugin.jvm.JvmConstant;

/**
 * @author haibin
 * @date 2019-04-23
 */
public class SystemGcFlagSpec implements FlagSpec {
    @Override
    public String getName() {
        return JvmConstant.FLAG_NAME_ENABLE_SYSTEM_GC;
    }

    @Override
    public String getDesc() {
        return "invoke System.gc() after stop injection,option value:[true,false],default value is true";
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
