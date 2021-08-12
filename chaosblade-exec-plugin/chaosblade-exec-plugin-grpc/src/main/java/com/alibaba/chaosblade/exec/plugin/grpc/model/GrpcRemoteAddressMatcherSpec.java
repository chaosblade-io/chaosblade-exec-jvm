package com.alibaba.chaosblade.exec.plugin.grpc.model;

import com.alibaba.chaosblade.exec.common.model.matcher.BasePredicateMatcherSpec;
import com.alibaba.chaosblade.exec.plugin.grpc.GrpcConstant;

/**
 * @Author wb-shd671576
 * @package: com.alibaba.chaosblade.exec.plugin.grpc.model
 * @Date 2021-08-05
 */
public class GrpcRemoteAddressMatcherSpec extends BasePredicateMatcherSpec {

    @Override
    public String getName() {
        return GrpcConstant.GET_REMOTE_ADDRESS;
    }

    @Override
    public String getDesc() {
        return "The grpc remote address which used";
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
