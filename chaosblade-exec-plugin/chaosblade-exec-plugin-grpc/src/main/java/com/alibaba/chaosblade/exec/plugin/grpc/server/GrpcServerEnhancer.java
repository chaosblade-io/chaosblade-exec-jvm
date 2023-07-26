package com.alibaba.chaosblade.exec.plugin.grpc.server;

import com.alibaba.chaosblade.exec.common.aop.BeforeEnhancer;
import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherModel;
import com.alibaba.chaosblade.exec.common.util.JsonUtil;
import com.alibaba.chaosblade.exec.common.util.ReflectUtil;
import com.alibaba.chaosblade.exec.plugin.grpc.GrpcConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * @Author wb-shd671576
 * @package: com.alibaba.chaosblade.exec.plugin.grpc.server
 * @Date 2021-08-05
 */
public class GrpcServerEnhancer extends BeforeEnhancer {

    private static final Logger LOGGER = LoggerFactory.getLogger(GrpcServerEnhancer.class);

    @Override
    public EnhancerModel doBeforeAdvice(ClassLoader classLoader, String className, Object object,
                                        Method method, Object[]
                                                methodArguments)
            throws Exception {
        if (methodArguments == null || object == null) {
            LOGGER.warn("The necessary parameters is null");
            return null;
        }

        Object ctx = methodArguments[0];
        Object pipeline = ReflectUtil.getSuperclassFieldValue(ctx, "pipeline", false);
        MatcherModel matcherModel = new MatcherModel();
        if(pipeline != null){
            Object channel = ReflectUtil.getSuperclassFieldValue(pipeline, "channel",false);
            if(channel != null){
                String remoteAddress = ReflectUtil.getSuperclassFieldValue(channel, "localAddress",false).toString();
                matcherModel.add(GrpcConstant.GET_REMOTE_ADDRESS, remoteAddress);
            }
        }
        LOGGER.debug("Grpc Server matchers: {}", JsonUtil.writer().writeValueAsString(matcherModel));
        EnhancerModel enhancerModel = new EnhancerModel(classLoader, matcherModel);
        enhancerModel.addMatcher(GrpcConstant.SERVER_KEY, "true");
        return enhancerModel;
    }


}
