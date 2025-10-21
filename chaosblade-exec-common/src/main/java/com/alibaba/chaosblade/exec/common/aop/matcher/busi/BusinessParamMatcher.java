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

package com.alibaba.chaosblade.exec.common.aop.matcher.busi;

import com.alibaba.chaosblade.exec.common.aop.CustomMatcher;
import com.alibaba.chaosblade.exec.common.util.BusinessParamUtil;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** @author wufunc@gmail.com */
public class BusinessParamMatcher implements CustomMatcher {
  private static final Logger LOGGER = LoggerFactory.getLogger(BusinessParamMatcher.class);

  private static final BusinessParamMatcher INSTANCE = new BusinessParamMatcher();

  private BusinessParamMatcher() {}

  public static BusinessParamMatcher getInstance() {
    return INSTANCE;
  }

  @Override
  public boolean match(String commandValue, Object originValue) {
    Map<String, String> businessData = (Map<String, String>) originValue;
    BusinessParamUtil.BusinessParamWrapper businessParamWrapper =
        BusinessParamUtil.parseFromJsonStr(commandValue);
    List<BusinessParamUtil.BusinessParam> businessParams = businessParamWrapper.getParams();
    BusinessParamPatternMatcher matcher =
        BusinessParamPatternEnum.getPatternMatcher(businessParamWrapper.getPattern());
    if (matcher == null) {
      LOGGER.debug(
          "b-params match fail,unsupported matching pattern, pattern:{}",
          businessParamWrapper.getPattern());
      return false;
    }
    return matcher.match(businessData, businessParams);
  }

  @Override
  public boolean regexMatch(String commandValue, Object originValue) {
    return false;
  }
}
