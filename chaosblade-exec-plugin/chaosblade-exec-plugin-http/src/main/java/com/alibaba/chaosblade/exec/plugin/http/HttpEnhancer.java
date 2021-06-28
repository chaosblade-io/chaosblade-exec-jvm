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

package com.alibaba.chaosblade.exec.plugin.http;

import com.alibaba.chaosblade.exec.common.aop.BeforeEnhancer;
import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.common.model.action.delay.BaseTimeoutExecutor;
import com.alibaba.chaosblade.exec.common.model.action.delay.TimeoutExecutor;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherModel;
import com.alibaba.chaosblade.exec.common.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.net.SocketTimeoutException;

/**
 * @Author yuhan
 * @package: com.alibaba.chaosblade.exec.plugin.http
 * @Date 2019-05-22 20:35
 */
public abstract class HttpEnhancer extends BeforeEnhancer {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpEnhancer.class);

    @Override
    public EnhancerModel doBeforeAdvice(ClassLoader classLoader, String className, Object object,
                                        Method method, Object[]
                                                methodArguments)
            throws Exception {
        MatcherModel matcherModel = new MatcherModel();
        matcherModel.add(HttpConstant.URI_KEY, getUrl(object, methodArguments));
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("http matchers: {}", JsonUtil.writer().writeValueAsString(matcherModel));
        }
        EnhancerModel enhancerModel = new EnhancerModel(classLoader, matcherModel);
        int timeout = getTimeout(object, methodArguments);
        postDoBeforeAdvice(enhancerModel);
        enhancerModel.setTimeoutExecutor(createTimeoutExecutor(classLoader, timeout, className));
        return enhancerModel;
    }

    /**
     * Get service timeout
     *
     * @param instance
     * @return
     */
    protected abstract int getTimeout(Object instance, Object[] methodArguments);

    /**
     * Create timeout executor
     *
     * @param classLoader
     * @param timeout
     * @param className
     * @return
     */
    protected TimeoutExecutor createTimeoutExecutor(ClassLoader classLoader, long timeout, String className) {
        return new BaseTimeoutExecutor(classLoader, timeout) {
            @Override
            public Exception generateTimeoutException(ClassLoader classLoader) {
                return new SocketTimeoutException("Read timed out");
            }
        };
    }


    protected abstract void postDoBeforeAdvice(EnhancerModel enhancerModel);

    /**
     * 获取Http Url
     *
     * @param instance
     * @param object
     * @return
     */
    protected abstract String getUrl(Object instance, Object[] object) throws Exception;

}
