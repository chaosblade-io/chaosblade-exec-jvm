/*
 * Copyright 1999-2019 Alibaba Group Holding Ltd.
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

import java.lang.reflect.Method;

import com.alibaba.chaosblade.exec.common.aop.BeforeEnhancer;
import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherModel;
import com.alibaba.chaosblade.exec.common.util.ReflectUtil;
import com.alibaba.chaosblade.exec.common.util.SQLParserUtil;
import com.alibaba.chaosblade.exec.common.util.SQLParserUtil.SqlType;
import com.alibaba.fastjson.JSON;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Changjun Xiao
 */
public class MysqlEnhancer extends BeforeEnhancer {

    private static final Logger LOGGER = LoggerFactory.getLogger(MysqlEnhancer.class);

    @Override
    public EnhancerModel doBeforeAdvice(ClassLoader classLoader, String className, Object object,
                                        Method method, Object[] methodArguments)
        throws Exception {
        if (methodArguments == null || object == null || methodArguments.length != 10) {
            LOGGER.warn("The necessary parameters is null or length is not equal 10, {}",
                methodArguments != null ? methodArguments.length : null);
            return null;
        }

        Object statement = methodArguments[0];
        Object query = methodArguments[1];
        Object catalog = methodArguments[8];

        String host = ReflectUtil.getFieldValue(object, "host", false);
        Integer port = ReflectUtil.getFieldValue(object, "port", false);
        String sql = null;
        String database = null;
        String table = null;

        if (query == null) {
            boolean isPreparedStatement = ReflectUtil.isAssignableFrom(classLoader, statement.getClass(),
                "com.mysql.jdbc.PreparedStatement");
            if (isPreparedStatement) {
                sql = ReflectUtil.invokeMethod(statement, "getPreparedSql", new Object[0], false);
            }
        } else {
            sql = (String)query;
        }
        if (catalog == null && table != null) {
            String[] dbAndTable = table.split(".");
            if (dbAndTable.length == 2) {
                database = dbAndTable[0];
            }
        } else {
            database = (String)catalog;
        }
        table = SQLParserUtil.findTableName(sql);
        SqlType type = SQLParserUtil.getSqlType(sql);

        MatcherModel matcherModel = new MatcherModel();
        matcherModel.add(MysqlConstant.HOST_MATCHER_NAME, host);
        matcherModel.add(MysqlConstant.TABLE_MATCHER_NAME, table);
        matcherModel.add(MysqlConstant.DATABASE_MATCHER_NAME, database);
        if (type != null) {
            matcherModel.add(MysqlConstant.SQL_TYPE_MATCHER_NAME, type.name());
        }
        if (port != null) {
            matcherModel.add(MysqlConstant.PORT_MATCHER_NAME, port.toString());
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("mysql matchers: {}", JSON.toJSONString(matcherModel));
        }
        return new EnhancerModel(classLoader, matcherModel);
    }
}
