package com.alibaba.chaosblade.exec.plugin.jvm.oom.flag;

import com.alibaba.chaosblade.exec.common.model.FlagSpec;
import com.alibaba.chaosblade.exec.plugin.jvm.JvmConstant;

/**
 * @author RinaisSuper
 * @date 2019-04-24
 * @email rinalhb@icloud.com
 */

public class ThreadCountFlagSpec implements FlagSpec {
    @Override
    public String getName() {
        return JvmConstant.FLAG_NAME_THREAD_COUNT;
    }

    @Override
    public String getDesc() {
        return "Thread count to make oom error,if you want to speed up the oom.default value is :"
            + JvmConstant.FLAG_VALUE_OOM_THREAD_COUNT;
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
