package com.alibaba.chaosblade.exec.plugin.hdfs.restful.webhdfs;

import com.alibaba.chaosblade.exec.common.aop.BeforeEnhancer;
import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherModel;
import com.alibaba.chaosblade.exec.common.util.JsonUtil;
import com.alibaba.chaosblade.exec.common.util.ReflectUtil;
import com.alibaba.chaosblade.exec.common.util.StringUtils;
import com.alibaba.chaosblade.exec.plugin.hdfs.common.HdfsConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

public class HdfsWebHdfsEnhancer extends BeforeEnhancer {
    private static final Logger LOGGER = LoggerFactory.getLogger(HdfsWebHdfsEnhancer.class);

    @Override
    public EnhancerModel doBeforeAdvice(ClassLoader classLoader, String className, Object object, Method method,
                                        Object[] methodArguments) throws Exception {
        MatcherModel matcherModel = new MatcherModel();
        if (methodArguments[4] != null) {
            String filePath = ReflectUtil.invokeMethod(methodArguments[4], "getAbsolutePath");
            if (StringUtils.isNotBlank(filePath)) {
                matcherModel.add(HdfsConstant.FLAG_COMMON_FILE, filePath.trim());
            }
        }
        if (methodArguments[5] != null) {
            Object operationParam = ReflectUtil.invokeMethod(methodArguments[5], "getValue");
            if (operationParam != null) {
                String operation = ReflectUtil.invokeMethod(operationParam, "name");
                if (StringUtils.isNotBlank(operation)) {
                    matcherModel.add(HdfsConstant.FLAG_RESTFUL_OPERATION, operation.trim().toLowerCase());
                }
            }
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("{} matchers: {}", getClass().getName(), JsonUtil.writer().writeValueAsString(matcherModel));
        }

        return new EnhancerModel(classLoader, matcherModel);
    }
}
