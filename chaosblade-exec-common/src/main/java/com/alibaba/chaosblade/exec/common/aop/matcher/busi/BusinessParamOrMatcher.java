package com.alibaba.chaosblade.exec.common.aop.matcher.busi;

import com.alibaba.chaosblade.exec.common.util.BusinessParamUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class BusinessParamOrMatcher implements BusinessParamPatternMatcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(BusinessParamOrMatcher.class);

    @Override
    public boolean match(Map<String, String> businessData, List<BusinessParamUtil.BusinessParam> businessParams) {
        for (BusinessParamUtil.BusinessParam businessParam : businessParams) {
            if (!businessData.containsKey(businessParam.getKey())) {
                continue;
            }
            String requestValue = businessData.get(businessParam.getKey());
            if (requestValue.equals(businessParam.getValue())) {
                return true;
            }
        }
        LOGGER.debug("b-params match fail,request data:{},command value:{}", businessData, businessParams);
        return false;
    }
}
