package com.alibaba.chaosblade.exec.plugin.grpc.client;

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
 * @package: com.alibaba.chaosblade.exec.plugin.grpc.client
 * @Date 2021-08-05
 */
public class GrpcClientEnhancer extends BeforeEnhancer {

    private static final Logger LOGGER = LoggerFactory.getLogger(GrpcClientEnhancer.class);

    @Override
    public EnhancerModel doBeforeAdvice(ClassLoader classLoader, String className, Object object,
                                        Method method, Object[]
                                                methodArguments)
            throws Exception {
        if (methodArguments == null || object == null) {
            LOGGER.warn("The necessary parameters is null");
            return null;
        }

        Object cmd = methodArguments[1];
        Object stream = ReflectUtil.getFieldValue(cmd, "stream", false);
        MatcherModel matcherModel = new MatcherModel();
        if(stream != null){
            Object requestMethod = ReflectUtil.getSuperclassFieldValue(stream, "method",false);
            if(requestMethod != null){
                String fullMethod = ReflectUtil.invokeMethod(requestMethod, "getFullMethodName", new Object[] {}, false).toString();
                matcherModel.add(GrpcConstant.GET_METHOD, fullMethod);
            }
            Object channel = ReflectUtil.getSuperclassFieldValue(stream, "channel",false);
            if(channel != null){
                String remoteAddress = ReflectUtil.getSuperclassFieldValue(channel, "remoteAddress",false).toString();
                matcherModel.add(GrpcConstant.GET_REMOTE_ADDRESS, remoteAddress);
            }
        }

        LOGGER.debug("Grpc Client matchers: {}", JsonUtil.writer().writeValueAsString(matcherModel));
        EnhancerModel enhancerModel = new EnhancerModel(classLoader, matcherModel);
        enhancerModel.addMatcher(GrpcConstant.CLIENT_KEY, "true");
        return enhancerModel;
    }


}
