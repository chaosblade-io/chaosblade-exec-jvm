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

package com.alibaba.chaosblade.exec.plugin.servlet;

import com.alibaba.chaosblade.exec.common.aop.BeforeEnhancer;
import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.common.aop.matcher.busi.BusinessParamMatcher;
import com.alibaba.chaosblade.exec.common.constant.ModelConstant;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherModel;
import com.alibaba.chaosblade.exec.common.util.BusinessParamUtil;
import com.alibaba.chaosblade.exec.common.util.JsonUtil;
import com.alibaba.chaosblade.exec.common.util.ReflectUtil;
import com.alibaba.chaosblade.exec.common.util.StringUtils;
import com.alibaba.chaosblade.exec.spi.BusinessDataGetter;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** @author Changjun Xiao */
public class ServletEnhancer extends BeforeEnhancer {

  private static final Logger LOGGER = LoggerFactory.getLogger(ServletEnhancer.class);

  @Override
  public EnhancerModel doBeforeAdvice(
      ClassLoader classLoader,
      String className,
      Object object,
      Method method,
      Object[] methodArguments)
      throws Exception {
    Object request = methodArguments[0];
    String requestURI =
        ReflectUtil.invokeMethod(request, ServletConstant.GET_REQUEST_URI, new Object[] {}, false);
    String requestMethod =
        ReflectUtil.invokeMethod(request, ServletConstant.GET_METHOD, new Object[] {}, false);

    MatcherModel matcherModel = new MatcherModel();
    matcherModel.add(ServletConstant.METHOD_KEY, requestMethod);
    matcherModel.add(ServletConstant.REQUEST_PATH_KEY, requestURI);
    matcherModel.add(ServletConstant.REQUEST_PATH_REGEX_PATTERN_KEY, requestURI);
    LOGGER.debug("servlet matchers: {}", JsonUtil.writer().writeValueAsString(matcherModel));

    Map<String, Object> queryString = getQueryString(requestMethod, request);
    LOGGER.debug("origin params: {}", JsonUtil.writer().writeValueAsString(queryString));

    EnhancerModel enhancerModel = new EnhancerModel(classLoader, matcherModel);
    enhancerModel.addCustomMatcher(
        ServletConstant.QUERY_STRING_KEY, queryString, ServletParamsMatcher.getInstance());
    enhancerModel.addCustomMatcher(
        ServletConstant.QUERY_STRING_REGEX_PATTERN_KEY,
        queryString,
        ServletParamsMatcher.getInstance());
    try {
      Map<String, Map<String, String>> businessParams = getBusinessParams(request);
      enhancerModel.addCustomMatcher(
          ModelConstant.BUSINESS_PARAMS, businessParams, BusinessParamMatcher.getInstance());
    } catch (Exception e) {
      LOGGER.warn("Getting business params occurs exception,return null", e);
    }
    return enhancerModel;
  }

  private Map<String, Object> getQueryString(String method, Object request) throws Exception {
    Map<String, Object> params = new HashMap<String, Object>();
    if ("get".equalsIgnoreCase(method)) {
      String queryString =
          ReflectUtil.invokeMethod(
              request, ServletConstant.GET_QUERY_STRING, new Object[] {}, false);
      if (StringUtils.isNotBlank(queryString)) {
        queryString = URLDecoder.decode(queryString, System.getProperty("file.encoding"));
        String[] paramsStr = queryString.split(ServletConstant.AND_SYMBOL);
        for (String s : paramsStr) {
          int i = s.indexOf(ServletConstant.EQUALS_SYMBOL);
          if (i != -1) {
            params.put(s.substring(0, i), s.substring(i + 1));
          }
        }
      }
    } else {
      Map<String, String[]> parameterMap =
          ReflectUtil.invokeMethod(
              request, ServletConstant.GET_PARAMETER_MAP, new Object[] {}, false);
      Set<Map.Entry<String, String[]>> entries = parameterMap.entrySet();
      for (Map.Entry<String, String[]> entry : entries) {
        String value = "";
        String[] values = entry.getValue();
        if (values.length > 0) {
          value = values[0];
        }
        params.put(entry.getKey(), value);
      }
    }
    return params;
  }

  private Map<String, Map<String, String>> getBusinessParams(final Object invocation)
      throws Exception {
    return BusinessParamUtil.getAndParse(
        ServletConstant.TARGET_NAME,
        new BusinessDataGetter() {
          @Override
          public String get(String key) throws Exception {
            return ReflectUtil.invokeMethod(
                invocation, ServletConstant.GET_HEADER, new String[] {key}, false);
          }
        });
  }
}
