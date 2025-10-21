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

package com.alibaba.chaosblade.exec.plugin.hdfs.restful.httpfs;

import com.alibaba.chaosblade.exec.common.aop.PointCut;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.ClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.NameClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.MethodMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.NameMethodMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.OrMethodMatcher;

public class HdfsHttpFSPointCut implements PointCut {
  private static final String CLASS_HDFS_HTTPFS_SERVER =
      "org.apache.hadoop.fs.http.server.HttpFSServer";
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
    orMethodMatcher
        .or(new NameMethodMatcher(METHOD_HDFS_HTTPFS_GET))
        .or(new NameMethodMatcher(METHOD_HDFS_HTTPFS_PUT))
        .or(new NameMethodMatcher(METHOD_HDFS_HTTPFS_POST))
        .or(new NameMethodMatcher(METHOD_HDFS_HTTPFS_DELETE));
    return orMethodMatcher;
  }
}
