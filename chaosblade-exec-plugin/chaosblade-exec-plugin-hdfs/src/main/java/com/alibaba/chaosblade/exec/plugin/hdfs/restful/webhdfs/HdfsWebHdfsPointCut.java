/*
 * Copyright 1999-2013 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alibaba.chaosblade.exec.plugin.hdfs.restful.webhdfs;

import com.alibaba.chaosblade.exec.common.aop.PointCut;
import com.alibaba.chaosblade.exec.common.aop.matcher.MethodInfo;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.ClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.NameClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.MethodMatcher;
import java.util.Arrays;

public class HdfsWebHdfsPointCut implements PointCut {
    // NameNode handler
    private static final String CLASS_HDFS_WEBHDFS_METHODS = "org.apache.hadoop.hdfs.server.namenode.web.resources.NamenodeWebHdfsMethods";

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
        MethodMatcher methodMatcher = new MethodMatcher() {
            @Override
            public boolean isMatched(String methodName, MethodInfo methodInfo) {
                String[] parameterTypes = methodInfo.getParameterTypes();
                if (parameterTypes == null || parameterTypes.length < 5) {
                    return false;
                }

                return Arrays.asList(TARGET_METHODS).contains(methodName)
                       && String.class.getTypeName().equals(methodInfo.getParameterTypes()[4]);
            }
        };

        return methodMatcher;
    }
}
