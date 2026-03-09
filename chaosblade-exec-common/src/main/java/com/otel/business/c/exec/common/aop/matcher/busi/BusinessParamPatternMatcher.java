package com.otel.business.c.exec.common.aop.matcher.busi;

import com.otel.business.c.exec.common.util.BusinessParamUtil;
import java.util.List;
import java.util.Map;

public interface BusinessParamPatternMatcher {
  boolean match(
      Map<String, String> businessData, List<BusinessParamUtil.BusinessParam> businessParams);
}
