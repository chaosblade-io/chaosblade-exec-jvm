package com.alibaba.chaosblade.exec.common.aop.matcher.busi;

import com.alibaba.chaosblade.exec.common.aop.CustomMatcher;
import com.alibaba.chaosblade.exec.common.util.BusinessParamUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * @author wufunc@gmail.com
 */
public class BusinessParamMatcher implements CustomMatcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(BusinessParamMatcher.class);

    private static final BusinessParamMatcher INSTANCE = new BusinessParamMatcher();

    private BusinessParamMatcher() {
    }

    public static BusinessParamMatcher getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean match(String commandValue, Object originValue) {
        Map<String, String> businessData = (Map<String, String>) originValue;
        List<BusinessParamUtil.BusinessParam> businessParams = BusinessParamUtil.parseFromJsonStr(commandValue);
        for (BusinessParamUtil.BusinessParam businessParam : businessParams) {
            if (!businessData.containsKey(businessParam.getKey())) {
                LOGGER.debug("b-params match fail,command value does not contains key:{}", businessParam.getKey());
                return false;
            }
            String requestValue = businessData.get(businessParam.getKey());
            if (!requestValue.equals(businessParam.getValue())) {
                LOGGER.debug("b-params match fail,origin value:{},command value:{}", requestValue, businessParam.getValue());
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean regexMatch(String commandValue, Object originValue) {
        return false;
    }
}
