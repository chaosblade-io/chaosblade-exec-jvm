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

package com.alibaba.chaosblade.exec.plugin.servlet;

import com.alibaba.chaosblade.exec.common.aop.BeforeEnhancer;
import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherModel;
import com.alibaba.chaosblade.exec.common.util.ReflectUtil;
import com.alibaba.chaosblade.exec.common.util.StringUtils;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * @author Changjun Xiao
 */
public class ServletEnhancer extends BeforeEnhancer {

    private static final Logger LOOGER = LoggerFactory.getLogger(ServletEnhancer.class);

    @Override
    public EnhancerModel doBeforeAdvice(ClassLoader classLoader, String className, Object object,
                                        Method method, Object[] methodArguments)
        throws Exception {
        Object request = methodArguments[0];
        String pathInfo = ReflectUtil.invokeMethod(request, "getPathInfo", new Object[] {}, false);
        String queryString = ReflectUtil.invokeMethod(request, "getQueryString", new Object[] {}, false);
        String servletPath = ReflectUtil.invokeMethod(request, "getServletPath", new Object[] {}, false);
        String requestMethod = ReflectUtil.invokeMethod(request, "getMethod", new Object[] {}, false);
        // RequestUri without ContextPath
        String requestPath = StringUtils.isNotBlank(pathInfo) ? servletPath + pathInfo : servletPath;

        MatcherModel matcherModel = new MatcherModel();
        matcherModel.add(ServletConstant.PATH_INFO_KEY, pathInfo);
        matcherModel.add(ServletConstant.QUERY_STRING_KEY, queryString);
        matcherModel.add(ServletConstant.SERVLET_PATH_KEY, servletPath);
        matcherModel.add(ServletConstant.METHOD_KEY, requestMethod);
        matcherModel.add(ServletConstant.REQUEST_PATH_KEY, requestPath);

        LOOGER.info("servlet matchers: {}", JSON.toJSONString(matcherModel));

        return new EnhancerModel(classLoader, matcherModel);
    }
}
