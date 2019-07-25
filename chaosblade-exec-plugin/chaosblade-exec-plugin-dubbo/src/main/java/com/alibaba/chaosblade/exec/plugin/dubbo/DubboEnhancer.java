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

package com.alibaba.chaosblade.exec.plugin.dubbo;

import java.lang.reflect.Method;

import com.alibaba.chaosblade.exec.common.aop.BeforeEnhancer;
import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.common.model.action.delay.TimeoutExecutor;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherModel;
import com.alibaba.chaosblade.exec.common.util.ReflectUtil;
import com.alibaba.chaosblade.exec.plugin.dubbo.model.DubboThreadPoolFullExecutor;
import com.alibaba.fastjson.JSON;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Changjun Xiao
 */
public abstract class DubboEnhancer extends BeforeEnhancer {

    public static final String DEFAULT_VERSION = "0.0.0";
    public static final String GET_URL = "getUrl";
    public static final String GET_PARAMETER = "getParameter";
    public static final String APPLICATION_KEY = "application";
    public static final String GET_SERVICE_KEY = "getServiceKey";
    public static final String GET_METHOD_NAME = "getMethodName";
    public static final String SPLIT_TOKEN = ":";
    public static final String GROUP_SEP = "/";
    public static final String GET_INVOKER = "getInvoker";
    public static final String RECEIVED_METHOD = "received";
    public static final int INVALID_POS = -1;
    private static final Logger LOGGER = LoggerFactory.getLogger(DubboEnhancer.class);

    @Override
    public EnhancerModel doBeforeAdvice(ClassLoader classLoader, String className, Object object,
                                        Method method, Object[]
                                            methodArguments)
        throws Exception {
        if (method.getName().equals(RECEIVED_METHOD)) {
            // received method for thread pool experiment
            DubboThreadPoolFullExecutor.INSTANCE.setWrappedChannelHandler(object);
            return null;
        }

        Object invocation = methodArguments[0];
        if (object == null || invocation == null) {
            LOGGER.warn("The necessary parameter is null.");
            return null;
        }
        Object url = getUrl(object, invocation);
        if (url == null) {
            LOGGER.warn("Url is null, can not get necessary values.");
            return null;
        }
        String appName = ReflectUtil.invokeMethod(url, GET_PARAMETER, new Object[] {APPLICATION_KEY}, false);
        String methodName = ReflectUtil.invokeMethod(invocation, GET_METHOD_NAME, new Object[0], false);
        String[] serviceAndVersionGroup = getServiceNameWithVersionGroup(invocation, url);

        MatcherModel matcherModel = new MatcherModel();
        matcherModel.add(DubboConstant.APP_KEY, appName);
        matcherModel.add(DubboConstant.SERVICE_KEY, serviceAndVersionGroup[0]);
        matcherModel.add(DubboConstant.VERSION_KEY, serviceAndVersionGroup[1]);
        if (2 < serviceAndVersionGroup.length &&
            null != serviceAndVersionGroup[2]) {
            matcherModel.add(DubboConstant.GROUP_KEY, serviceAndVersionGroup[2]);
        }
        matcherModel.add(DubboConstant.METHOD_KEY, methodName);
        int timeout = getTimeout(methodName, object, invocation);
        matcherModel.add(DubboConstant.TIMEOUT_KEY, timeout + "");

        LOGGER.info("dubbo matchers: {}", JSON.toJSONString(matcherModel));

        EnhancerModel enhancerModel = new EnhancerModel(classLoader, matcherModel);
        enhancerModel.setTimeoutExecutor(createTimeoutExecutor(classLoader, timeout));

        postDoBeforeAdvice(enhancerModel);
        return enhancerModel;
    }

    protected abstract void postDoBeforeAdvice(EnhancerModel enhancerModel);

    /**
     * Get service name with version
     *
     * @param invocation
     * @param url
     * @return
     * @throws Exception
     */
    public String[] getServiceNameWithVersionGroup(Object invocation, Object url) throws Exception {
        // com.alibaba.dubbo.demo.DemoService | com.alibaba.dubbo.demo.DemoService:1.0
        String serviceKey = ReflectUtil.invokeMethod(url, GET_SERVICE_KEY, new Object[0], false);
        if (serviceKey == null) {
            LOGGER.warn("Service key is null in dubbo consumer. invocation: {}", invocation);
            return null;
        } else {
            int groupSep = serviceKey.indexOf(GROUP_SEP);
            String group = null;
            if (INVALID_POS != groupSep) {
                group = serviceKey.substring(0, groupSep);
                serviceKey = serviceKey.substring(groupSep + 1);
            }

            String[] serviceAndVersion = serviceKey.split(SPLIT_TOKEN);
            if (serviceAndVersion.length == 1) {
                return new String[] {serviceAndVersion[0], DEFAULT_VERSION, group};
            }
            return new String[] {serviceAndVersion[0], serviceAndVersion[1], group};
        }
    }

    /**
     * Get service timeout
     *
     * @param method
     * @param instance
     * @param invocation
     * @return
     */
    protected abstract int getTimeout(String method, Object instance, Object invocation);

    /**
     * Get service url
     *
     * @param instance
     * @param invocation
     * @return
     * @throws Exception
     */
    protected abstract Object getUrl(Object instance, Object invocation) throws Exception;

    /**
     * Create timeout executor
     *
     * @param classLoader
     * @param timeout
     * @return
     */
    protected abstract TimeoutExecutor createTimeoutExecutor(ClassLoader classLoader, long timeout);

}
