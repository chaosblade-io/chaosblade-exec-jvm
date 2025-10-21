/*
 * Copyright 2025 The ChaosBlade Authors
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
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.ClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.NameClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.AndMethodMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.MethodMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.NameMethodMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.OrMethodMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.ParameterMethodMatcher;

public class HdfsWebHdfsPointCut implements PointCut {
  // NameNode handler
  private static final String CLASS_HDFS_WEBHDFS_METHODS =
      "org.apache.hadoop.hdfs.server.namenode.web.resources.NamenodeWebHdfsMethods";

  private static final String METHOD_HDFS_WEBHDFS_GET = "get";
  private static final String METHOD_HDFS_WEBHDFS_PUT = "put";
  private static final String METHOD_HDFS_WEBHDFS_POST = "post";
  private static final String METHOD_HDFS_WEBHDFS_DELETE = "delete";

  @Override
  public ClassMatcher getClassMatcher() {
    return new NameClassMatcher(CLASS_HDFS_WEBHDFS_METHODS);
  }

  @Override
  public MethodMatcher getMethodMatcher() {
    OrMethodMatcher methodNameMatchers = new OrMethodMatcher();
    methodNameMatchers
        .or(new NameMethodMatcher(METHOD_HDFS_WEBHDFS_GET))
        .or(new NameMethodMatcher(METHOD_HDFS_WEBHDFS_PUT))
        .or(new NameMethodMatcher(METHOD_HDFS_WEBHDFS_POST))
        .or(new NameMethodMatcher(METHOD_HDFS_WEBHDFS_DELETE));

    ParameterMethodMatcher parameterMethodMatcher =
        new ParameterMethodMatcher(
            new String[] {
              "org.apache.hadoop.security.UserGroupInformation",
              "org.apache.hadoop.hdfs.web.resources.DelegationParam",
              "org.apache.hadoop.hdfs.web.resources.UserParam",
              "org.apache.hadoop.hdfs.web.resources.DoAsParam",
              "java.lang.String"
            },
            5,
            ParameterMethodMatcher.GREAT_THAN);

    return new AndMethodMatcher().and(methodNameMatchers).and(parameterMethodMatcher);
  }
}
