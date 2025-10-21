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

package com.alibaba.chaosblade.exec.plugin.mysql;

import com.alibaba.chaosblade.exec.common.aop.BeforeEnhancer;
import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherModel;
import com.alibaba.chaosblade.exec.common.util.JsonUtil;
import com.alibaba.chaosblade.exec.common.util.ReflectUtil;
import com.alibaba.chaosblade.exec.common.util.SQLParserUtil;
import java.lang.reflect.Method;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author yefei
 * @create 2020-11-19 16:14
 */
public class ApacheShardingJdbcEnhancer extends BeforeEnhancer {

  private static final Logger logger = LoggerFactory.getLogger(ApacheShardingJdbcEnhancer.class);

  @Override
  public EnhancerModel doBeforeAdvice(
      ClassLoader classLoader,
      String className,
      Object object,
      Method method,
      Object[] methodArguments)
      throws Exception {
    if (methodArguments == null || object == null) {
      logger.warn("The necessary parameters is");
      return null;
    }

    Object executionGroup = methodArguments[0];

    List<Object> inputs = ReflectUtil.getSuperclassFieldValue(executionGroup, "inputs", false);
    Object storageResource =
        ReflectUtil.getSuperclassFieldValue(inputs.get(0), "storageResource", false);
    Object delegate = ReflectUtil.getSuperclassFieldValue(storageResource, "delegate", false);
    Object protocol = ReflectUtil.getSuperclassFieldValue(delegate, "protocol", false);
    Object currentHost = ReflectUtil.getSuperclassFieldValue(protocol, "currentHost", false);
    Object database = ReflectUtil.getSuperclassFieldValue(protocol, "database", false);

    Object sqlQuery = ReflectUtil.getSuperclassFieldValue(delegate, "sqlQuery", false);
    String sql = sqlQuery.toString();

    String host = ReflectUtil.getSuperclassFieldValue(currentHost, "host", false);
    Integer port = ReflectUtil.getSuperclassFieldValue(currentHost, "port", false);

    String databaseStr = database.toString();

    String table = SQLParserUtil.findTableName(sql);
    if (databaseStr == null && table != null) {
      String[] dbAndTable = table.split(".");
      if (dbAndTable.length == 2) {
        databaseStr = dbAndTable[0];
      }
    }

    SQLParserUtil.SqlType type = SQLParserUtil.getSqlType(sql);

    MatcherModel matcherModel = new MatcherModel();
    matcherModel.add(MysqlConstant.HOST_MATCHER_NAME, host);
    matcherModel.add(MysqlConstant.TABLE_MATCHER_NAME, table);
    matcherModel.add(MysqlConstant.DATABASE_MATCHER_NAME, databaseStr);
    if (type != null) {
      matcherModel.add(MysqlConstant.SQL_TYPE_MATCHER_NAME, type.name());
    }
    if (port != null) {
      matcherModel.add(MysqlConstant.PORT_MATCHER_NAME, port.toString());
    }
    if (logger.isDebugEnabled()) {
      logger.debug("mysql matchers: {}", JsonUtil.writer().writeValueAsString(matcherModel));
    }
    return new EnhancerModel(classLoader, matcherModel);
  }
}
