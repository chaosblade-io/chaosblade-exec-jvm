package com.alibaba.chaosblade.exec.plugin.hdfs.restful.webhdfs;

import com.alibaba.chaosblade.exec.common.aop.PointCut;
import com.alibaba.chaosblade.exec.common.aop.matcher.MethodInfo;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.ClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.NameClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.MethodMatcher;
import javassist.bytecode.AccessFlag;
import java.util.Arrays;

public class HdfsWebHdfsPointCut implements PointCut {
    // NameNode handler
    private static final String CLASS_HDFS_WEBHDFS_METHODS = "org.apache.hadoop.hdfs.server.namenode.web.resources.NamenodeWebHdfsMethods";

    // DataNode handler, including operations such as OPEN, CREATE, APPEND, GETFILECHECKSUM, etc.
    // private static final String CLASS_HDFS_WEBHDFS_HANDLER = "org.apache.hadoop.hdfs.server.datanode.web.webhdfs.WebHdfsHandler";

    private static final String METHOD_HDFS_WEBHDFS_GET = "get";
    private static final String METHOD_HDFS_WEBHDFS_PUT = "put";
    private static final String METHOD_HDFS_WEBHDFS_POST = "post";
    private static final String METHOD_HDFS_WEBHDFS_DELETE = "delete";
    private static final String[] TARGET_METHODS = new String[] {
            METHOD_HDFS_WEBHDFS_GET,
            METHOD_HDFS_WEBHDFS_PUT,
            METHOD_HDFS_WEBHDFS_POST,
            METHOD_HDFS_WEBHDFS_DELETE
    };

    @Override
    public ClassMatcher getClassMatcher() {
        return new NameClassMatcher(CLASS_HDFS_WEBHDFS_METHODS);
    }

    @Override
    public MethodMatcher getMethodMatcher() {
        return (String methodName, MethodInfo methodInfo) ->
                Arrays.asList(TARGET_METHODS).contains(methodName)
                && (methodInfo.getAccess() & AccessFlag.PUBLIC) == AccessFlag.PUBLIC;
    }
}
