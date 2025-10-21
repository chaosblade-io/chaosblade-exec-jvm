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

import com.alibaba.chaosblade.exec.common.aop.BeforeEnhancer;
import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherModel;
import com.alibaba.chaosblade.exec.common.util.JsonUtil;
import com.alibaba.chaosblade.exec.plugin.elasticsearch.index.RequestIndexProvider;
import java.lang.reflect.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author yuhan
 *
 * @package: com.alibaba.chaosblade.exec.plugin.elasticsearch @Date 2019-05-22 20:35
 */
public class ElasticSearchEnhancer extends BeforeEnhancer {

  private static final Logger LOGGER = LoggerFactory.getLogger(ElasticSearchEnhancer.class);

  @Override
  public EnhancerModel doBeforeAdvice(
      ClassLoader classLoader,
      String className,
      Object object,
      Method method,
      Object[] methodArguments)
      throws Exception {
    MatcherModel matcherModel = new MatcherModel();

    matcherModel.add(ElasticSearchConstant.INDEX, getIndex(methodArguments));
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("http matchers: {}", JsonUtil.writer().writeValueAsString(matcherModel));
    }

    return new EnhancerModel(classLoader, matcherModel);
  }

  private String getIndex(Object[] args) {
    if (null == args) {
      return null;
    }
    if (args.length == 3) {
      return String.valueOf(args[0]);
    } else if (RequestIndexProvider.isRequest(args[1])) {
      return RequestIndexProvider.get(args[1]).getIndexOfString(args[1]);
    }
    return RequestIndexProvider.get(args[0]).getIndexOfString(args[0]);
  }
}
