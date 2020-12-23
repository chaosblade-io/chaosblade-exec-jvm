/*
 * Copyright 1999-2020 Alibaba Group Holding Ltd.
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * @author yefei
 * @create 2020-11-19 16:14
 */
public class Mysql5Enhancer extends BeforeEnhancer {

    private static final Logger logger = LoggerFactory.getLogger(Mysql5Enhancer.class);

    @Override
    public EnhancerModel doBeforeAdvice(ClassLoader classLoader, String className, Object object, Method method, Object[] methodArguments) throws Exception {
        if (methodArguments == null || object == null) {
            logger.warn("The necessary parameters is null");
            return null;
        }

        Object statement = methodArguments[0];
        Object query = methodArguments[1];
        Object catalog;
        if (methodArguments.length == 10) {
            // mysql 5.1.x
            catalog = methodArguments[8];
        } else if (methodArguments.length == 11) {
            // mysql 5.0.x
            catalog = methodArguments[9];
        } else {
            logger.warn("The necessary parameters is null or length is not equal 10 or 11, {}",
                    methodArguments != null ? methodArguments.length : null);
            return null;
        }

        String host = ReflectUtil.getFieldValue(object, "host", false);
        Integer port = ReflectUtil.getFieldValue(object, "port", false);
        String sql = (String) query;
        String database = null;

        if (ReflectUtil.isAssignableFrom(classLoader, statement.getClass(), "com.mysql.jdbc.PreparedStatement")) {
            sql = ReflectUtil.getSuperclassFieldValue(statement, "originalSql", false);
        }

        String table = SQLParserUtil.findTableName(sql);
        if (catalog == null && table != null) {
            String[] dbAndTable = table.split(".");
            if (dbAndTable.length == 2) {
                database = dbAndTable[0];
            }
        } else {
            database = (String) catalog;
        }
        SQLParserUtil.SqlType type = SQLParserUtil.getSqlType(sql);

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
        if (logger.isDebugEnabled()) {
            logger.debug("mysql matchers: {}", JsonUtil.writer().writeValueAsString(matcherModel));
        }
        return new EnhancerModel(classLoader, matcherModel);
    }
}
