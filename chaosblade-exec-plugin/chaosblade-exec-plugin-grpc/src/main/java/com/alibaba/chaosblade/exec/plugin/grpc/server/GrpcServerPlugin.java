package com.alibaba.chaosblade.exec.plugin.grpc.server;

import com.alibaba.chaosblade.exec.common.aop.Enhancer;
import com.alibaba.chaosblade.exec.common.aop.PointCut;
import com.alibaba.chaosblade.exec.plugin.grpc.GrpcConstant;
import com.alibaba.chaosblade.exec.plugin.grpc.GrpcPlugin;

/**
 * @Author wb-shd671576
 * @package: com.alibaba.chaosblade.exec.plugin.grpc.server
 * @Date 2021-08-05
 */
public class GrpcServerPlugin extends GrpcPlugin {

    @Override
    public String getName() { return GrpcConstant.SERVER_KEY;}

    @Override
    public PointCut getPointCut() {
        return new GrpcServerPointCut();
    }

    @Override
    public Enhancer getEnhancer() {
        return new GrpcServerEnhancer();
    }
}
