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

import com.alibaba.chaosblade.exec.common.util.BusinessParamUtil;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BusinessParamAndMatcher implements BusinessParamPatternMatcher {

  private static final Logger LOGGER = LoggerFactory.getLogger(BusinessParamAndMatcher.class);

  @Override
  public boolean match(
      Map<String, String> businessData, List<BusinessParamUtil.BusinessParam> businessParams) {
    for (BusinessParamUtil.BusinessParam businessParam : businessParams) {
      if (!businessData.containsKey(businessParam.getKey())) {
        LOGGER.debug(
            "b-params match fail,command value does not contains key:{}", businessParam.getKey());
        return false;
      }
      String requestValue = businessData.get(businessParam.getKey());
      if (!requestValue.equals(businessParam.getValue())) {
        LOGGER.debug(
            "b-params match fail,origin value:{},command value:{}",
            requestValue,
            businessParam.getValue());
        return false;
      }
    }
    return true;
  }
}
