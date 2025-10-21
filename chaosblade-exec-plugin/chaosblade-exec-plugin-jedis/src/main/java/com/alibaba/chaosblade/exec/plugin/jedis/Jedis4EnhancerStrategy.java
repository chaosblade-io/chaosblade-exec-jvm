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

package com.alibaba.chaosblade.exec.plugin.jedis;

import static com.alibaba.chaosblade.exec.plugin.jedis.JedisEnhancer.JEDIS_4_PARAMS_LENGTH;

import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherModel;
import com.alibaba.chaosblade.exec.common.util.JsonUtil;
import com.alibaba.chaosblade.exec.common.util.ReflectUtil;
import java.util.Iterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author liuhq <a href="15669072513@163.com">final RedisOutputStream os, final CommandArguments
 */
public class Jedis4EnhancerStrategy implements JedisMultiVersionStrategy {
  private final Logger LOGGER = LoggerFactory.getLogger(Jedis4EnhancerStrategy.class);

  @Override
  public EnhancerModel process(ClassLoader classLoader, Object[] methodArguments) throws Exception {
    Object command = methodArguments[1];
    Iterator iterator = ((Iterable) command).iterator();
    String[] cmdAndKeyArray = new String[2];
    for (int i = 0; i < JEDIS_4_PARAMS_LENGTH; i++) {
      Object obj = iterator.next();
      Object cmdBytes = ReflectUtil.invokeMethod(obj, "getRaw", new Object[0], false);
      if (cmdBytes.getClass().isArray() && (cmdBytes instanceof byte[])) {
        cmdAndKeyArray[i] = new String((byte[]) cmdBytes);
      }
    }
    MatcherModel matcherModel = new MatcherModel();
    if (cmdAndKeyArray[0] != null) {
      matcherModel.add(JedisConstant.COMMAND_TYPE_MATCHER_NAME, cmdAndKeyArray[0].toLowerCase());
    }
    if (cmdAndKeyArray[1] != null) {
      matcherModel.add(JedisConstant.KEY_MATCHER_NAME, cmdAndKeyArray[1]);
    }
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("jedis matchers: {}", JsonUtil.writer().writeValueAsString(matcherModel));
    }
    return new EnhancerModel(classLoader, matcherModel);
  }
}
