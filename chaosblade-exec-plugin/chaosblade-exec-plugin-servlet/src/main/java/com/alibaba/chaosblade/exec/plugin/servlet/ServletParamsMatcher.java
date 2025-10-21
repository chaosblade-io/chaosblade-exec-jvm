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

import com.alibaba.chaosblade.exec.common.aop.CustomMatcher;
import java.util.Map;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author yefei
 * @create 2020-06-22 14:33
 */
public class ServletParamsMatcher implements CustomMatcher {

  private static final Logger logger = LoggerFactory.getLogger(ServletParamsMatcher.class);

  private static final ServletParamsMatcher CALL_BACK = new ServletParamsMatcher();

  private ServletParamsMatcher() {}

  public static ServletParamsMatcher getInstance() {
    return CALL_BACK;
  }

  @Override
  public boolean match(String queryString, Object originValue) {
    Map<String, Object> value = (Map<String, Object>) originValue;
    String[] paramsStr = queryString.split(ServletConstant.AND_SYMBOL);
    for (String s : paramsStr) {
      int i = s.indexOf(ServletConstant.EQUALS_SYMBOL);
      if (i == -1) {
        return false;
      }
      Object actualValue = value.get(s.substring(0, i));
      String expectValue = s.substring(i + 1);
      if (actualValue == null) {
        logger.debug("query string mather fail, actualValue is null, expectValue:{}", expectValue);
        return false;
      }
      if (!expectValue.equals(actualValue.toString())) {
        logger.debug(
            "query string mather fail, actualValue: {}, expectValue:{}", actualValue, expectValue);
        return false;
      }
    }
    return true;
  }

  @Override
  public boolean regexMatch(String queryString, Object originValue) {
    Map<String, Object> value = (Map<String, Object>) originValue;
    String[] paramsStr = queryString.split(ServletConstant.AND_SYMBOL);
    for (String s : paramsStr) {
      int i = s.indexOf(ServletConstant.EQUALS_SYMBOL);
      if (i == -1) {
        return false;
      }
      Object actualValue = value.get(s.substring(0, i));
      String expectValue = s.substring(i + 1);
      if (actualValue == null) {
        logger.debug("query string mather fail, actualValue is null, expectValue:{}", expectValue);
        return false;
      }
      if (!Pattern.matches(expectValue, String.valueOf(actualValue))) {
        logger.debug(
            "query string mather fail, actualValue: {}, expectValue:{}", actualValue, expectValue);
        return false;
      }
    }
    return true;
  }
}
