package com.alibaba.chaosblade.exec.plugin.jvm.oom.flag;

import java.util.Arrays;

import com.alibaba.chaosblade.exec.common.model.FlagSpec;
import com.alibaba.chaosblade.exec.plugin.jvm.JvmConstant;
import com.alibaba.chaosblade.exec.plugin.jvm.oom.JvmMemoryArea;

/**
 * @author haibin
 * @date 2019-04-18
 * @email haibin.lhb@alibaba-inc.com
 */
public class JvmMemoryAreaFlagSpec implements FlagSpec {

    @Override
    public String getName() {
        return JvmConstant.FLAG_NAME_MEMORY_AREA;
    }

    @Override
    public String getDesc() {
        return "the jvm memory area you want  to cause OutOfMemoryError,the options:" + Arrays.toString(
            JvmMemoryArea.getAreaNames());
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
