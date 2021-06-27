package com.alibaba.chaosblade.exec.plugin.jvm.gc;

import com.alibaba.chaosblade.exec.common.model.FlagSpec;
import com.alibaba.chaosblade.exec.plugin.jvm.JvmConstant;

/**
 * @author shizhi.zhu@qunar.com
 */
public class FullGCEffectCountFlagSpec implements FlagSpec {
    @Override
    public String getName() {
        return JvmConstant.FLAG_FULL_GC_TOTAL_COUNT;
    }

    @Override
    public String getDesc() {
        return "total count of full gc actions";
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
