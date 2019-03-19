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

package com.alibaba.chaosblade.exec.plugin.dubbo.consumer;

import java.lang.reflect.Constructor;
import java.util.List;

import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.common.model.action.delay.BaseTimeoutExecutor;
import com.alibaba.chaosblade.exec.common.model.action.delay.TimeoutExecutor;
import com.alibaba.chaosblade.exec.common.util.ReflectUtil;
import com.alibaba.chaosblade.exec.plugin.dubbo.DubboConstant;
import com.alibaba.chaosblade.exec.plugin.dubbo.DubboEnhancer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Changjun Xiao
 */
public class DubboConsumerEnhancer extends DubboEnhancer {

    public static final String RPC_CONTEXT = "com.alibaba.dubbo.rpc.RpcContext";
    public static final String GET_CONTEXT = "getContext";
    public static final String GET_INVOKERS = "getInvokers";
    public static final String REGISTRY_DIRECTORY$_INVOKER_DELEGETE
        = "com.alibaba.dubbo.registry.integration.RegistryDirectory$InvokerDelegete";
    public static final String GET_PROVIDER_URL = "getProviderUrl";
    public static final int TIMEOUT_EXCEPTION_CODE = 2;
    public static final String TIMEOUT_KEY = "timeout";
    public static final int DEFAULT_TIMEOUT = 1000;
    public static final String GET_METHOD_PARAMETER = "getMethodParameter";
    private static final Logger LOGGER = LoggerFactory.getLogger(DubboConsumerEnhancer.class);
    private static final String DUBBO_TIMEOUT_EXCEPTION = "com.alibaba.dubbo.rpc.RpcException";

    /**
     * Get service url
     *
     * @param instance
     * @param invocation
     * @return
     * @throws Exception
     */
    @Override
    protected Object getUrl(Object instance, Object invocation) throws Exception {
        String rpcContextClassName = RPC_CONTEXT;
        // load class
        Class<?> rpcContextClass = instance.getClass().getClassLoader().loadClass(rpcContextClassName);
        Object context = ReflectUtil.invokeStaticMethod(rpcContextClass, GET_CONTEXT, new Object[0], false);
        if (context == null) {
            LOGGER.warn("Rpc context is null, can not get the url of provider.");
        } else {
            List<Object> invokers = ReflectUtil.invokeMethod(context, GET_INVOKERS, new Object[0], false);
            if (invokers == null || invokers.size() == 0) {
                LOGGER.warn("Get invokers from rpcContext is empty, can not get the url of provider.");
            } else {
                for (Object invoker : invokers) {
                    if (!invoker.getClass().getName().equals(
                        REGISTRY_DIRECTORY$_INVOKER_DELEGETE)) {
                        continue;
                    }
                    Object providerUrl = ReflectUtil.invokeMethod(invoker, GET_PROVIDER_URL, new Object[0], false);
                    if (providerUrl != null) {
                        return providerUrl;
                    }
                }
            }
        }
        LOGGER.info("Can not get the url of provider.");
        return ReflectUtil.invokeMethod(instance, GET_URL, new Object[0], false);
    }

    @Override
    protected void postDoBeforeAdvice(EnhancerModel enhancerModel) {
        enhancerModel.addMatcher(DubboConstant.CONSUMER_KEY, "true");
    }

    @Override
    protected int getTimeout(String method, Object instance, Object invocation) {
        try {
            Object url = ReflectUtil.invokeMethod(instance, GET_URL, new Object[0], false);
            if (url != null) {
                return ReflectUtil.invokeMethod(url, GET_METHOD_PARAMETER, new Object[] {method, TIMEOUT_KEY,
                    DEFAULT_TIMEOUT}, false);
            }
        } catch (Exception e) {
            LOGGER.warn("Getting timeout from url occurs exception. return default value " + DEFAULT_TIMEOUT, e);
        }
        return DEFAULT_TIMEOUT;
    }

    @Override
    protected TimeoutExecutor createTimeoutExecutor(ClassLoader classLoader, long timeout) {
        return new BaseTimeoutExecutor(classLoader, timeout) {
            @Override
            public Exception generateTimeoutException(ClassLoader classLoader) {
                Class timeOutExceptionClass;
                try {
                    timeOutExceptionClass = classLoader.loadClass(DUBBO_TIMEOUT_EXCEPTION);

                    Class[] paramTypes = {int.class, String.class};
                    Object[] params = {TIMEOUT_EXCEPTION_CODE,
                        "chaosblade-mock-TimeoutException,timeout=" + timeoutInMillis};
                    Constructor con = timeOutExceptionClass.getConstructor(paramTypes);
                    return (Exception)con.newInstance(params);
                } catch (ClassNotFoundException e) {
                    LOGGER.error("chaosblade-dubbo", "Can not find " + DUBBO_TIMEOUT_EXCEPTION, e);
                } catch (Exception e) {
                    LOGGER.error("chaosblade-dubbo", "Can not generate " + DUBBO_TIMEOUT_EXCEPTION, e);
                }
                return new RuntimeException(DubboConstant.TIMEOUT_EXCEPTION_MSG);
            }
        };
    }
}
