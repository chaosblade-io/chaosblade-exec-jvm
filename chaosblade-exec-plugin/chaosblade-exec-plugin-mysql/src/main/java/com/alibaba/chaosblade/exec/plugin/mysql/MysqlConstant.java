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

/** @author Changjun Xiao */
public interface MysqlConstant {

  String TARGET_NAME = "mysql";

  String HOST_MATCHER_NAME = "host";
  String TABLE_MATCHER_NAME = "select";
  String DATABASE_MATCHER_NAME = "database";
  String SQL_TYPE_MATCHER_NAME = "sqltype";
  String PORT_MATCHER_NAME = "port";

  String MYSQL_IO_CLASS = "com.mysql.jdbc.MysqlIO";
  String INTERCEPTOR_PRE_METHOD = "sqlQueryDirect";
  String MYSQL_SERVER_PREPARED_STMT_CLASS = "com.mysql.jdbc.ServerPreparedStatement";
  String MYSQL_SERVER_PREPARED_STMT_METHOD = "serverExecute";

  String MYSQL8_NATIVE_SESSION_CLASS = "com.mysql.cj.NativeSession";
  String MYSQL8_NATIVE_SESSION_METHOD = "execSQL";
  String MYSQL8_SERVER_PREPARED_STMT_CLASS = "com.mysql.cj.jdbc.ServerPreparedStatement";
  String MYSQL8SERVER_PREPARED_STMT_METHOD = "serverExecute";

  String AWS_MYSQL_NATIVE_SESSION_CLASS =
      "software.aws.rds.jdbc.mysql.shading.com.mysql.cj.NativeSession";
  String AWS_MYSQL_NATIVE_SESSION_METHOD = "execSQL";

  String IO_SHARDING_STATEMENT_EXECUTOR_CLASS =
      "io.shardingsphere.shardingjdbc.executor.AbstractStatementExecutor";
  String APACHE_SHARDING_STATEMENT_EXECUTOR_CLASS =
      "org.apache.shardingsphere.shardingjdbc.executor.AbstractStatementExecutor";
  String IO_SHARDING_STATEMENT_EXECUTOR_METHOD = "executeCallback";

  String APACHE_SHARDING_EXECUTOR_ENGINE_CLASS =
      "org.apache.shardingsphere.infra.executor.kernel.ExecutorEngine";
  String APACHE_SHARDING_EXECUTOR_ENGINE_METHOD = "syncExecute";
}
