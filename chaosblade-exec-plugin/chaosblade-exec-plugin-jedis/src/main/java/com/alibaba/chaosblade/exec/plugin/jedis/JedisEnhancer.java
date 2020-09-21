/*
 * Copyright 1999-2019 Alibaba Group Holding Ltd.
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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.chaosblade.exec.common.aop.BeforeEnhancer;
import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherModel;
import com.alibaba.chaosblade.exec.common.util.JsonUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author guoping.yao <a href="mailto:bryan880901@qq.com">
 */
public class JedisEnhancer extends BeforeEnhancer {

    public static final String CHARSET = "UTF-8";
    private static final Logger LOGGER = LoggerFactory.getLogger(JedisEnhancer.class);

    /**
     * final RedisOutputStream os, final byte[] command, final byte[]... args
     */
    @Override
    public EnhancerModel doBeforeAdvice(ClassLoader classLoader, String className, Object object,
                                        Method method, Object[] methodArguments)
        throws Exception {
        if (methodArguments == null || methodArguments.length != 3) {
            LOGGER.info("The necessary parameters is null or length is not equal 3, {}",
                methodArguments != null ? methodArguments.length : null);
            return null;
        }
        Object command = methodArguments[1];
        if (!(command instanceof byte[])) {
            return null;
        }

        Object args = methodArguments[2];
        if (!args.getClass().isArray() || !(args instanceof byte[][])) {
            return null;
        }
        String cmd = new String((byte[])command, CHARSET);
        List<String> sargs = new ArrayList<String>();

        byte[][] bargs = (byte[][])args;
        for (int i = 0; i < bargs.length; i++) {
            sargs.add(new String(bargs[i], CHARSET));
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
