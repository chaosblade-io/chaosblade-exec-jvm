package com.alibaba.chaosblade.exec.common.aop.matcher.busi;

import com.alibaba.chaosblade.exec.common.aop.CustomMatcher;
import com.alibaba.chaosblade.exec.common.util.BusinessParamUtil;

import java.util.List;
import java.util.Map;

/**
 * @author wufunc@gmail.com
 */
public class BusinessParamMatcher implements CustomMatcher {
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
                return false;
            }
            if (!businessData.get(businessParam.getKey()).equals(businessParam.getValue())) {
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
