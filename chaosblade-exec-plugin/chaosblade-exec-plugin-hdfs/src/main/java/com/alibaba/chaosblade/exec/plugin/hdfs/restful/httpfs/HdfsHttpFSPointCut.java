package com.alibaba.chaosblade.exec.plugin.hdfs.restful.httpfs;

import com.alibaba.chaosblade.exec.common.aop.PointCut;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.ClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.NameClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.MethodMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.NameMethodMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.OrMethodMatcher;

public class HdfsHttpFSPointCut implements PointCut {
    private static final String CLASS_HDFS_HTTPFS_SERVER = "org.apache.hadoop.fs.http.server.HttpFSServer";
    public static final String METHOD_HDFS_HTTPFS_GET = "get";
    public static final String METHOD_HDFS_HTTPFS_PUT = "put";
    public static final String METHOD_HDFS_HTTPFS_POST = "post";
    public static final String METHOD_HDFS_HTTPFS_DELETE = "delete";

    @Override
    public ClassMatcher getClassMatcher() {
        return new NameClassMatcher(CLASS_HDFS_HTTPFS_SERVER);
    }

    @Override
    public MethodMatcher getMethodMatcher() {
        OrMethodMatcher orMethodMatcher = new OrMethodMatcher();
        orMethodMatcher.or(new NameMethodMatcher(METHOD_HDFS_HTTPFS_GET))
                .or(new NameMethodMatcher(METHOD_HDFS_HTTPFS_PUT))
                .or(new NameMethodMatcher(METHOD_HDFS_HTTPFS_POST))
                .or(new NameMethodMatcher(METHOD_HDFS_HTTPFS_DELETE));
        return orMethodMatcher;
    }
}
