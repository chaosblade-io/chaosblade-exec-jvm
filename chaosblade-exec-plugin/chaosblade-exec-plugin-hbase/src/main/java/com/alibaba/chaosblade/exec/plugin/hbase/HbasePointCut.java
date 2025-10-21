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

import com.alibaba.chaosblade.exec.common.aop.PointCut;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.ClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.NameClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.OrClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.MethodMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.NameMethodMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.OrMethodMatcher;

/**
 * @Author Yuhan Tang
 *
 * @package: com.alibaba.chaosblade.exec.plugin.hbase @Date 2020-10-30 14:42
 */
public class HbasePointCut implements PointCut {

  private static final String HBASE_TABLE_CLASS = "org.apache.hadoop.hbase.TableName";
  private static final String HBASE_VALUEOF_METHOD = "valueOf";
  private static final String HBASE_COLUMN_CLASS = "org.apache.hadoop.hbase.client.Get";
  private static final String HBASE_ADDCOLUMN_METHOD = "addFamily";

  @Override
  public ClassMatcher getClassMatcher() {
    OrClassMatcher orClassMatcher = new OrClassMatcher();
    orClassMatcher
        .or(new NameClassMatcher(HBASE_TABLE_CLASS))
        .or(new NameClassMatcher(HBASE_COLUMN_CLASS));
    return orClassMatcher;
  }

  @Override
  public MethodMatcher getMethodMatcher() {
    OrMethodMatcher orMethodMatcher = new OrMethodMatcher();
    orMethodMatcher
        .or(new NameMethodMatcher(HBASE_VALUEOF_METHOD))
        .or(new NameMethodMatcher(HBASE_ADDCOLUMN_METHOD));
    return orMethodMatcher;
  }
}
