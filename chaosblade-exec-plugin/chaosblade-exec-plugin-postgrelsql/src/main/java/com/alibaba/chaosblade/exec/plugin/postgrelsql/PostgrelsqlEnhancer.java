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

package com.alibaba.chaosblade.exec.plugin.postgrelsql;

import com.alibaba.chaosblade.exec.common.aop.BeforeEnhancer;
import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherModel;
import com.alibaba.chaosblade.exec.common.util.JsonUtil;
import com.alibaba.chaosblade.exec.common.util.ReflectUtil;
import com.alibaba.chaosblade.exec.common.util.SQLParserUtil;
import com.alibaba.chaosblade.exec.common.util.SQLParserUtil.SqlType;
import java.lang.reflect.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** @author guoping.yao <a href="mailto:bryan880901@qq.com"> */
public class PostgrelsqlEnhancer extends BeforeEnhancer {

  private static final Logger LOGGER = LoggerFactory.getLogger(PostgrelsqlEnhancer.class);

  /**
   * Object[] methodArguments Query query, ParameterList parameters, ResultHandler handler, int
   * maxRows, int fetchSize, int flags
   */
  @Override
  public EnhancerModel doBeforeAdvice(
      ClassLoader classLoader,
      String className,
      Object object,
      Method method,
      Object[] methodArguments)
      throws Exception {
    if (methodArguments == null || object == null || methodArguments.length != 6) {
      LOGGER.info(
          "The necessary parameters is null or length is not equal 6, {}",
          methodArguments != null ? methodArguments.length : null);
      return null;
    }
    Object query = methodArguments[0];
    if (query == null) {
      LOGGER.info("query is null, can not get sql");
      return null;
    }

    Object pgStream = ReflectUtil.getSuperclassFieldValue(object, "pgStream", false);
    Object hostSpec = ReflectUtil.getFieldValue(pgStream, "hostSpec", false);
    String host = ReflectUtil.getFieldValue(hostSpec, "host", false);
    Integer port = ReflectUtil.getFieldValue(hostSpec, "port", false);

    String sql = query.toString();
    String table = SQLParserUtil.findTableName(sql);
    SqlType type = SQLParserUtil.getSqlType(sql);
    String database = ReflectUtil.invokeMethod(object, "getDatabase", new Object[0], false);

    MatcherModel matcherModel = new MatcherModel();
    matcherModel.add(PostgrelsqlConstant.HOST_MATCHER_NAME, host);
    matcherModel.add(PostgrelsqlConstant.TABLE_MATCHER_NAME, table);
    matcherModel.add(PostgrelsqlConstant.DATABASE_MATCHER_NAME, database);
    if (type != null) {
      matcherModel.add(PostgrelsqlConstant.SQL_TYPE_MATCHER_NAME, type.name());
    }
    if (port != null) {
      matcherModel.add(PostgrelsqlConstant.PORT_MATCHER_NAME, port.toString());
    }
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("postgrelsql matchers: {}", JsonUtil.writer().writeValueAsString(matcherModel));
    }
    return new EnhancerModel(classLoader, matcherModel);
  }
}
