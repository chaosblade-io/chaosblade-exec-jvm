package com.alibaba.chaosblade.exec.common.aop.matcher.busi;

import com.alibaba.chaosblade.exec.common.aop.CustomMatcher;
import com.alibaba.chaosblade.exec.common.util.BusinessParamUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
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
        BusinessParamUtil.BusinessParamWrapper businessParamWrapper = BusinessParamUtil.parseFromJsonStr(commandValue);
        List<BusinessParamUtil.BusinessParam> businessParams = businessParamWrapper.getParams();
        BusinessParamPatternMatcher matcher = BusinessParamPatternEnum.getPatternMatcher(businessParamWrapper.getPattern());
        if (matcher == null) {
            LOGGER.debug("b-params match fail,unsupported matching pattern, pattern:{}", businessParamWrapper.getPattern());
            return false;
        }
        return matcher.match(businessData, businessParams);
    }

    @Override
    public boolean regexMatch(String commandValue, Object originValue) {
        return false;
    }
}
