package com.alibaba.chaosblade.exec.plugin.grpc.model;

import com.alibaba.chaosblade.exec.common.aop.PredicateResult;
import com.alibaba.chaosblade.exec.common.model.FrameworkModelSpec;
import com.alibaba.chaosblade.exec.common.model.Model;
import com.alibaba.chaosblade.exec.common.model.action.ActionSpec;
import com.alibaba.chaosblade.exec.common.model.action.delay.DelayActionSpec;
import com.alibaba.chaosblade.exec.common.model.action.exception.ThrowCustomExceptionActionSpec;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherModel;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherSpec;
import com.alibaba.chaosblade.exec.plugin.grpc.GrpcConstant;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @Author wb-shd671576
 * @package: com.alibaba.chaosblade.exec.plugin.grpc.model
 * @Date 2021-08-05
 */
public class GrpcModelSpec extends FrameworkModelSpec {

    public GrpcModelSpec() {
        addActionExample();
    }

    private void addActionExample() {
        List<ActionSpec> actions = getActions();
        for (ActionSpec action : actions) {
            if (action instanceof DelayActionSpec) {
                action.setLongDesc("Grpc delay experiment");
                action.setExample("# Do a delay 2s experiment for grpc client operations\n"
                        + "blade create grpc delay --remoteAddress /127.0.0.1:50051 --method hello.Greeter/SayHello  --time 2000 --client\n\n");
            }
            if (action instanceof ThrowCustomExceptionActionSpec) {
                action.setLongDesc("Grpc throws customer exception experiment");
                action.setExample("# Do a throws customer exception experiment for grpc operations\n" +
                        "blade create grpc throwCustomException --exception java.lang.Exception --remoteAddress /127.0.0.1:50051 --method hello.Greeter/SayHello --client");
            }
        }
    }

    @Override
    protected List<MatcherSpec> createNewMatcherSpecs() {
        ArrayList<MatcherSpec> matcherSpecs = new ArrayList<MatcherSpec>();
        matcherSpecs.add(new GrpcServerMatcherSpec());
        matcherSpecs.add(new GrpcClientMatcherSpec());
        matcherSpecs.add(new GrpcMethodMatcherSpec());
        matcherSpecs.add(new GrpcRemoteAddressMatcherSpec());
        return matcherSpecs;
    }

    @Override
    public String getTarget() {
        return GrpcConstant.TARGET_NAME;
    }

    @Override
    public String getShortDesc() {
        return "grpc experiment!";
    }

    @Override
    public String getLongDesc() {
        return "grpc experiment contains delay and exception";
    }

}
