package com.alibaba.chaosblade.exec.plugin.log.model;

import com.alibaba.chaosblade.exec.common.model.matcher.BasePredicateMatcherSpec;
import com.alibaba.chaosblade.exec.plugin.log.LogConstant;

/**
 * @author shizhi.zhu@qunar.com
 */
public class LogbackModelSpec extends BasePredicateMatcherSpec {
    @Override
    public String getName() {
        return LogConstant.LOGBACK_KEY;
    }

    @Override
    public String getDesc() {
        return "To tag logback experiment.";
    }

    @Override
    public boolean noArgs() {
        return true;
    }

    @Override
    public boolean required() {
        return false;
    }
}
