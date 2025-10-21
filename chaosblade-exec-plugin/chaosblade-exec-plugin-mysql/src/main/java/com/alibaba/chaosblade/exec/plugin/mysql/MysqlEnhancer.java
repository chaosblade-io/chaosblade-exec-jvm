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

import static com.alibaba.chaosblade.exec.plugin.mysql.MysqlConstant.*;

import com.alibaba.chaosblade.exec.common.aop.BeforeEnhancer;
import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import java.lang.reflect.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** @author Changjun Xiao */
public class MysqlEnhancer extends BeforeEnhancer {

  private static final Logger LOGGER = LoggerFactory.getLogger(MysqlEnhancer.class);

  private final Mysql5Enhancer mysql5Enhancer = new Mysql5Enhancer();

  private final Mysql8Enhancer mysql8Enhancer = new Mysql8Enhancer();

  private final MysqlAwsEnhancer mysqlAwsEnhancer = new MysqlAwsEnhancer();

  private final IoShardingJdbcEnhancer ioShardingJdbcEnhancer = new IoShardingJdbcEnhancer();

  private final ApacheShardingJdbcEnhancer apacheShardingJdbcEnhancer =
      new ApacheShardingJdbcEnhancer();

  @Override
  public EnhancerModel doBeforeAdvice(
      ClassLoader classLoader,
      String className,
      Object object,
      Method method,
      Object[] methodArguments)
      throws Exception {

    if (MYSQL_IO_CLASS.equals(className) || MYSQL_SERVER_PREPARED_STMT_CLASS.equals(className)) {
      return mysql5Enhancer.doBeforeAdvice(classLoader, className, object, method, methodArguments);
    } else if (AWS_MYSQL_NATIVE_SESSION_CLASS.equals(className)) {
      return mysqlAwsEnhancer.doBeforeAdvice(
          classLoader, className, object, method, methodArguments);
    } else if (IO_SHARDING_STATEMENT_EXECUTOR_CLASS.equals(className)
        || APACHE_SHARDING_STATEMENT_EXECUTOR_CLASS.equals(className)) {
      return ioShardingJdbcEnhancer.doBeforeAdvice(
          classLoader, className, object, method, methodArguments);
    } else if (APACHE_SHARDING_EXECUTOR_ENGINE_CLASS.equals(className)) {
      return apacheShardingJdbcEnhancer.doBeforeAdvice(
          classLoader, className, object, method, methodArguments);
    } else {
      return mysql8Enhancer.doBeforeAdvice(classLoader, className, object, method, methodArguments);
    }
  }
}
