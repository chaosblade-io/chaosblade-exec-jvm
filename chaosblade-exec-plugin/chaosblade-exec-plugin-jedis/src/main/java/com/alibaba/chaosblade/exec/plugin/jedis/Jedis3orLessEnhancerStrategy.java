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

import static com.alibaba.chaosblade.exec.plugin.jedis.JedisEnhancer.CHARSET;

import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherModel;
import com.alibaba.chaosblade.exec.common.util.JsonUtil;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author guoping.yao <a href="mailto:bryan880901@qq.com">
 * @author liuhq <a href="15669072513@163.com">final RedisOutputStream os, final byte[] command,
 *     final byte[]... args
 */
public class Jedis3orLessEnhancerStrategy implements JedisMultiVersionStrategy {
  private final Logger LOGGER = LoggerFactory.getLogger(Jedis3orLessEnhancerStrategy.class);

  @Override
  public EnhancerModel process(ClassLoader classLoader, Object[] methodArguments) throws Exception {
    Object command = methodArguments[1];
    Object args = methodArguments[2];
    if (!args.getClass().isArray() || !(args instanceof byte[][])) {
      return null;
    }
    String cmd;
    List<String> sargs = new ArrayList<String>();
    byte[][] bargs = (byte[][]) args;

    cmd = new String((byte[]) command, CHARSET);
    for (byte[] barg : bargs) {
      sargs.add(new String(barg, CHARSET));
    }

    String key = null;
    if (sargs.size() > 0) {
      key = sargs.get(0);
    }

    MatcherModel matcherModel = new MatcherModel();
    matcherModel.add(JedisConstant.COMMAND_TYPE_MATCHER_NAME, cmd.toLowerCase());
    if (key != null) {
      matcherModel.add(JedisConstant.KEY_MATCHER_NAME, key);
    }
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("jedis matchers: {}", JsonUtil.writer().writeValueAsString(matcherModel));
    }
    return new EnhancerModel(classLoader, matcherModel);
  }
}
