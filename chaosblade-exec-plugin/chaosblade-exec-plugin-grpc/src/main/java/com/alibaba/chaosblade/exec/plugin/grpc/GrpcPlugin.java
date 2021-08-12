package com.alibaba.chaosblade.exec.plugin.grpc;

import com.alibaba.chaosblade.exec.common.aop.Plugin;
import com.alibaba.chaosblade.exec.common.model.ModelSpec;
import com.alibaba.chaosblade.exec.plugin.grpc.model.GrpcModelSpec;

/**
 * @Author wb-shd671576
 * @package: com.alibaba.chaosblade.exec.plugin.grpc
 * @Date 2021-08-05
 */
public abstract class GrpcPlugin implements Plugin {

    @Override
    public ModelSpec getModelSpec() {
        return new GrpcModelSpec();
    }

}
