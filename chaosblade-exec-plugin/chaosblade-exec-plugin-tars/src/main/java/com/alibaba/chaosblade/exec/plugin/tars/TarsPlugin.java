package com.alibaba.chaosblade.exec.plugin.tars;

import com.alibaba.chaosblade.exec.common.aop.Plugin;
import com.alibaba.chaosblade.exec.common.model.ModelSpec;
import com.alibaba.chaosblade.exec.plugin.tars.model.TarsModelSpec;

/**
 * @author saikei
 * @email lishiji@huya.com
 */
public abstract class TarsPlugin implements Plugin {
    @Override
    public ModelSpec getModelSpec() {
        return new TarsModelSpec();
    }
}
