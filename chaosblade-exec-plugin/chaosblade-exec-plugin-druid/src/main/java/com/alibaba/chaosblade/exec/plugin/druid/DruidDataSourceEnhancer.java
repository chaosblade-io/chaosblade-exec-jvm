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

package com.alibaba.chaosblade.exec.plugin.druid;

import com.alibaba.chaosblade.exec.common.aop.BeforeEnhancer;
import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import java.lang.reflect.Method;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** @author Changjun Xiao */
public class DruidDataSourceEnhancer extends BeforeEnhancer {
  private static final Logger LOGGER = LoggerFactory.getLogger(DruidDataSourceEnhancer.class);

  @Override
  public EnhancerModel doBeforeAdvice(
      ClassLoader classLoader,
      String className,
      Object object,
      Method method,
      Object[] methodArguments)
      throws Exception {
    if (object != null && DataSource.class.isInstance(object)) {
      LOGGER.debug("match the druid dataSource, object: {}", className);
      DruidConnectionPoolFullExecutor.INSTANCE.setDataSource(object);
    } else {
      LOGGER.debug(
          "the object is null or is not instance of DataSource class, object: {}",
          object == null ? null : object.getClass().getName());
    }
    return null;
  }
}
