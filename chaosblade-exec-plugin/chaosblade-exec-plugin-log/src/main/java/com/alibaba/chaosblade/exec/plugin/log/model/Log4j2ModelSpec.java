package com.alibaba.chaosblade.exec.plugin.log.model;

import com.alibaba.chaosblade.exec.common.model.matcher.BasePredicateMatcherSpec;
import com.alibaba.chaosblade.exec.plugin.log.LogConstant;

/**
 * @author shizhi.zhu@qunar.com
 */
public class Log4j2ModelSpec extends BasePredicateMatcherSpec {

    @Override
    public String getName() {
        return LogConstant.LOG4J2_KEY;
    }

    @Override
    public String getDesc() {
        return "To tag log4j2 experiment.";
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
