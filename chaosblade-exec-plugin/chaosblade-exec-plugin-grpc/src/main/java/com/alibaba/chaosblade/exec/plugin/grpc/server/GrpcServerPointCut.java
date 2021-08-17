package com.alibaba.chaosblade.exec.plugin.grpc.server;

import com.alibaba.chaosblade.exec.common.aop.PointCut;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.ClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.NameClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.MethodMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.NameMethodMatcher;


/**
 * @Author wb-shd671576
 * @package: com.alibaba.chaosblade.exec.plugin.grpc.server
 * @Date 2021-08-05
 */
public class GrpcServerPointCut implements PointCut {

    private static final String GRPC_SERVER_REST_CLASS = "io.grpc.netty.NettyServerHandler";
    private static final String GRPC_SERVER_REST_METHOD = "onHeadersRead";


    @Override
    public ClassMatcher getClassMatcher() {
        return new NameClassMatcher(GRPC_SERVER_REST_CLASS);
    }

    @Override
    public MethodMatcher getMethodMatcher() {
        return new NameMethodMatcher(GRPC_SERVER_REST_METHOD);
    }

}
