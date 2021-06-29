package com.alibaba.chaosblade.exec.plugin.log;

import com.alibaba.chaosblade.exec.common.aop.Plugin;
import com.alibaba.chaosblade.exec.common.model.ModelSpec;
import com.alibaba.chaosblade.exec.plugin.log.model.LogModelSpec;

/**
 * @author shizhi.zhu@qunar.com
 */
public abstract class LogPlugin implements Plugin {
    @Override
    public ModelSpec getModelSpec() {
        return new LogModelSpec();
    }
}
