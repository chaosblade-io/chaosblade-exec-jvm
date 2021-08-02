package com.alibaba.chaosblade.exec.plugin.gateway;

import com.alibaba.chaosblade.exec.common.model.matcher.BasePredicateMatcherSpec;

/**
 * @Author wb-shd671576
 * @package: com.alibaba.chaosblade.exec.plugin.gateway
 * @Date 2021-07-29
 */
public class GatewayPathMatcherSpec extends BasePredicateMatcherSpec {

    @Override
    public String getName() {
        return GatewayConstant.GET_REQUST_PATH;
    }

    @Override
    public String getDesc() {
        return "The gateway path which used";
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
