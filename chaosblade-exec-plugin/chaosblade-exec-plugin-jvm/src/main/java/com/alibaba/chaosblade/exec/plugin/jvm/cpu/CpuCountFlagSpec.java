package com.alibaba.chaosblade.exec.plugin.jvm.cpu;

import com.alibaba.chaosblade.exec.common.model.FlagSpec;
import com.alibaba.chaosblade.exec.plugin.jvm.JvmConstant;

/**
 * @author Changjun Xiao
 */
public class CpuCountFlagSpec implements FlagSpec {

    @Override
    public String getName() {
        return JvmConstant.FLAG_NAME_CPU_COUNT;
    }

    @Override
    public String getDesc() {
        return "Binding cpu core count";
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
