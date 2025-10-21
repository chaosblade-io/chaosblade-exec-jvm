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

package com.alibaba.chaosblade.exec.plugin.hbase;

import com.alibaba.chaosblade.exec.common.aop.BeforeEnhancer;
import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherModel;
import com.alibaba.chaosblade.exec.common.util.JsonUtil;
import com.alibaba.chaosblade.exec.common.util.StringUtils;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author Yuhan Tang
 *
 * @package: com.alibaba.chaosblade.exec.plugin.hbase @Date 2020-10-30 14:46
 */
public class HbaseEnhancer extends BeforeEnhancer {

  private static Charset charset = Charset.forName("UTF-8");

  private static final Logger LOGGER = LoggerFactory.getLogger(HbaseEnhancer.class);

  @Override
  public EnhancerModel doBeforeAdvice(
      ClassLoader classLoader,
      String className,
      Object object,
      Method method,
      Object[] methodArguments)
      throws Exception {
    if (methodArguments == null || methodArguments.length > 1) {
      LOGGER.info(
          "The necessary parameters is null or length is not equal 1,{}",
          methodArguments != null ? methodArguments.length : null);
      return null;
    }
    LOGGER.info("method Arguments {}", methodArguments.toString());
    MatcherModel matcherModel = new MatcherModel();
    matcherModel.add(HbaseConstant.TABLE, getTableName(methodArguments));
    matcherModel.add(HbaseConstant.Column, getColumnName(methodArguments));
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("hbase matchers: {}", JsonUtil.writer().writeValueAsString(matcherModel));
    }

    return new EnhancerModel(classLoader, matcherModel);
  }

  public String getTableName(Object[] args) {
    String tableName = null;
    if (args.length == 1) {
      Object obj = args[0];
      if (obj instanceof String) {
        tableName = (String) obj;
      } else if (obj instanceof byte[]) {
        tableName = new String((byte[]) obj);
      } else if (obj instanceof char[]) {
        tableName = new String((char[]) obj);
      }

    } else if (args.length == 2) {
      Object obj = args[1];
      if (obj instanceof String) {
        tableName = (String) obj;
      } else if (obj instanceof byte[]) {
        tableName = new String((byte[]) obj);
      } else if (obj instanceof char[]) {
        tableName = new String((char[]) obj);
      } else if (obj instanceof ByteBuffer) {
        ByteBuffer tableNameBuffer = (ByteBuffer) obj;
        tableName = getTableName(toString(tableNameBuffer));
      }
    }
    return tableName;
  }

  public String getColumnName(Object[] args) {
    String columnName = null;
    Object obj = args[0];
    if (obj instanceof String) {
      columnName = (String) obj;
    } else if (obj instanceof byte[]) {
      columnName = new String((byte[]) obj);
    } else if (obj instanceof char[]) {
      columnName = new String((char[]) obj);
    }
    return columnName;
  }

  private String toString(ByteBuffer buffer) {
    try {
      return charset.newDecoder().decode(buffer.asReadOnlyBuffer()).toString();
    } catch (CharacterCodingException e) {
      buffer.flip();
      byte[] data = new byte[buffer.limit()];
      buffer.get(data);
      return new String(data);
    }
  }

  private String getTableName(String tableName) {
    if (StringUtils.indexOf(tableName, ':') != -1) {
      int index = StringUtils.indexOf(tableName, ':');
      String namespace = StringUtils.substring(tableName, 0, index);
      String table = StringUtils.substring(tableName, index + 1);
      if (!table.startsWith("hbase")) {
        tableName = namespace + table;
        return tableName;
      }
    } else if (!tableName.startsWith("hbase")) {

      return tableName;
    }
    return tableName;
  }
}
