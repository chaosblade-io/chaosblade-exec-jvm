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

import com.alibaba.chaosblade.exec.common.aop.BeforeEnhancer;
import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.common.util.ReflectUtil;
import java.lang.reflect.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author guoping.yao <a href="mailto:bryan880901@qq.com">
 * @author liuhq <a href="15669072513@163.com">modified for compatible jedis 4.x
 */
public class JedisEnhancer extends BeforeEnhancer {
  private static final Logger LOGGER = LoggerFactory.getLogger(JedisEnhancer.class);

  protected static final String CHARSET = "UTF-8";
  protected static final String JEDIS_4_PARAM_CLASSNAME = "redis.clients.jedis.CommandArguments";
  protected static final Integer JEDIS_3_ORLESS_PARAMS_LENGTH = 3;
  protected static final Integer JEDIS_4_PARAMS_LENGTH = 2;
  protected static final Integer JEDIS_MIN_PARAMS_LENGTH =
      Math.min(JEDIS_4_PARAMS_LENGTH, JEDIS_3_ORLESS_PARAMS_LENGTH);

  /** compatible jedis 4.x or 3.x or less */
  @Override
  public EnhancerModel doBeforeAdvice(
      ClassLoader classLoader,
      String className,
      Object object,
      Method method,
      Object[] methodArguments)
      throws Exception {
    JedisMultiVersionStrategy jedisMultiVersionStrategy;
    if (methodArguments == null || methodArguments.length < JEDIS_MIN_PARAMS_LENGTH) {
      return null;
    }
    Object mehtedArgument = methodArguments[1];
    boolean isAssignFromJedis4 =
        ReflectUtil.isAssignableFrom(
            classLoader, mehtedArgument.getClass(), JEDIS_4_PARAM_CLASSNAME);
    boolean isAssignFromJedis3 = mehtedArgument instanceof byte[];
    if (methodArguments.length == JEDIS_3_ORLESS_PARAMS_LENGTH && isAssignFromJedis3) {
      jedisMultiVersionStrategy = new Jedis3orLessEnhancerStrategy();
    } else if (methodArguments.length == JEDIS_4_PARAMS_LENGTH && isAssignFromJedis4) {
      jedisMultiVersionStrategy = new Jedis4EnhancerStrategy();
    } else {
      LOGGER.info(
          "The necessary parameters is null or length is not equal 2 or 3, {}",
          methodArguments.length);
      return null;
    }
    return jedisMultiVersionStrategy.process(classLoader, methodArguments);
  }
}
