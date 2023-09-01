package com.alibaba.chaosblade.exec.plugin.hdfs.restful.httpfs;

import com.alibaba.chaosblade.exec.common.aop.BeforeEnhancer;
import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherModel;
import com.alibaba.chaosblade.exec.common.util.JsonUtil;
import com.alibaba.chaosblade.exec.common.util.StringUtils;
import com.alibaba.chaosblade.exec.plugin.hdfs.common.HdfsConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

public class HdfsHttpFSEnhancer extends BeforeEnhancer {
    private static final Logger LOGGER = LoggerFactory.getLogger(HdfsHttpFSEnhancer.class);

    @Override
    public EnhancerModel doBeforeAdvice(ClassLoader classLoader, String className, Object object, Method method,
                                        Object[] methodArguments) throws Exception {
        MatcherModel matcherModel = new MatcherModel();
        if (HdfsHttpFSPointCut.METHOD_HDFS_HTTPFS_GET.equals(method.getName())
            || HdfsHttpFSPointCut.METHOD_HDFS_HTTPFS_DELETE.equals(method.getName())) {
            if (StringUtils.isNotBlank(String.valueOf(methodArguments[0]))) {
                matcherModel.add(HdfsConstant.FLAG_COMMON_FILE, HdfsConstant.HDFS_ROOT_PATH + methodArguments[0]);
            }
            if (methodArguments[1] != null && StringUtils.isNotBlank(String.valueOf(methodArguments[1]))) {
                matcherModel.add(HdfsConstant.FLAG_RESTFUL_OPERATION, String.valueOf(methodArguments[1]).toLowerCase());
            }
        }
        if (HdfsHttpFSPointCut.METHOD_HDFS_HTTPFS_PUT.equals(method.getName())
            || HdfsHttpFSPointCut.METHOD_HDFS_HTTPFS_POST.equals(method.getName())) {
            if (StringUtils.isNotBlank(String.valueOf(methodArguments[2]))) {
                matcherModel.add(HdfsConstant.FLAG_COMMON_FILE, HdfsConstant.HDFS_ROOT_PATH + methodArguments[2]);
            }
            if (methodArguments[1] != null && StringUtils.isNotBlank(String.valueOf(methodArguments[3]))) {
                matcherModel.add(HdfsConstant.FLAG_RESTFUL_OPERATION, String.valueOf(methodArguments[3]).toLowerCase());
            }
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("{} matchers: {}", getClass().getName(), JsonUtil.writer().writeValueAsString(matcherModel));
        }

        return new EnhancerModel(classLoader, matcherModel);
    }
}
