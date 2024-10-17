package com.alibaba.chaosblade.exec.plugin.grpc.client;

import com.alibaba.chaosblade.exec.common.aop.PointCut;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.ClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.NameClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.MethodMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.NameMethodMatcher;


/**
 * @Author wb-shd671576
 * @package: com.alibaba.chaosblade.exec.plugin.grpc.client
 * @Date 2021-08-05
 */
public class GrpcClientPointCut implements PointCut {

    private static final String GRPC_CLIENT_REST_CLASS = "io.grpc.netty.NettyClientHandler";
    private static final String GRPC_CLIENT_REST_METHOD = "sendGrpcFrame";


    @Override
    public ClassMatcher getClassMatcher() {
        return new NameClassMatcher(GRPC_CLIENT_REST_CLASS);
    }

    @Override
    public MethodMatcher getMethodMatcher() {
        return new NameMethodMatcher(GRPC_CLIENT_REST_METHOD);
    }

}
