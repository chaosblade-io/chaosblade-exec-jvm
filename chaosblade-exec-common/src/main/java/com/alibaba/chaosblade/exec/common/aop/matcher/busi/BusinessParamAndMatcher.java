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
