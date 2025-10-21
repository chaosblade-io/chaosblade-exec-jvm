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

package com.alibaba.chaosblade.exec.plugin.elasticsearch;

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
 * @package: com.alibaba.chaosblade.exec.plugin.elasticsearch @Date 2020-10-30 14:42
 */
public class ElasticSearchPointCut implements PointCut {

  private static final String ES_REST_CLASS = "org.elasticsearch.client.RestHighLevelClient";
  private static final String ES_REST_METHOD_SYNC = "internalPerformRequest";
  private static final String ES_REST_METHOD_ASYNC = "internalPerformRequestAsync";

  private static final String ES_CLIENT_CLASS = "org.elasticsearch.client.support.AbstractClient";
  private static final String ES_CLIENT_METHOD_EXECUTE = "execute";
  private static final String ES_CLIENT_METHOD_INDEX = "prepareIndex";
  private static final String ES_CLIENT_METHOD_DELETE = "prepareDelete";
  private static final String ES_CLIENT_METHOD_GET = "prepareGet";

  @Override
  public ClassMatcher getClassMatcher() {
    return new OrClassMatcher()
        .or(new NameClassMatcher(ES_REST_CLASS))
        .or(new NameClassMatcher(ES_CLIENT_CLASS));
  }

  @Override
  public MethodMatcher getMethodMatcher() {
    OrMethodMatcher orMethodMatcher = new OrMethodMatcher();
    orMethodMatcher
        .or(new NameMethodMatcher(ES_REST_METHOD_SYNC))
        .or(new NameMethodMatcher(ES_REST_METHOD_ASYNC))
        .or(new NameMethodMatcher(ES_CLIENT_METHOD_EXECUTE))
        .or(new NameMethodMatcher(ES_CLIENT_METHOD_INDEX))
        .or(new NameMethodMatcher(ES_CLIENT_METHOD_DELETE))
        .or(new NameMethodMatcher(ES_CLIENT_METHOD_GET));
    return orMethodMatcher;
  }
}
