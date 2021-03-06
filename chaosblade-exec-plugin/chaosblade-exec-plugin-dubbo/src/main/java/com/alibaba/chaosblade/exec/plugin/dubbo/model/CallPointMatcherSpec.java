package com.alibaba.chaosblade.exec.plugin.dubbo.model;

import com.alibaba.chaosblade.exec.common.model.matcher.BasePredicateMatcherSpec;
import com.alibaba.chaosblade.exec.plugin.dubbo.DubboConstant;

/**
 * @author shizhi.zhu@qunar.com
 */
public class CallPointMatcherSpec extends BasePredicateMatcherSpec {
    @Override
    public String getName() {
        return DubboConstant.CALL_POINT_KEY;
    }

    @Override
    public String getDesc() {
        return "the class and method name who send the request";
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
