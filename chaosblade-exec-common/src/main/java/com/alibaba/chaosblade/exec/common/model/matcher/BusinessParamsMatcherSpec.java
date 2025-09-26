package com.alibaba.chaosblade.exec.common.model.matcher;

import com.alibaba.chaosblade.exec.common.aop.PredicateResult;
import com.alibaba.chaosblade.exec.common.aop.matcher.busi.BusinessParamPatternEnum;
import com.alibaba.chaosblade.exec.common.constant.ModelConstant;
import com.alibaba.chaosblade.exec.common.util.BusinessParamUtil;
import com.alibaba.chaosblade.exec.common.util.StringUtils;
import java.util.List;

/** @author wufunc@gmail.com */
public class BusinessParamsMatcherSpec extends BasePredicateMatcherSpec {
  @Override
  public String getName() {
    return ModelConstant.BUSINESS_PARAMS;
  }

  @Override
  public String getDesc() {
    return "business parmas";
  }

  @Override
  public boolean noArgs() {
    return false;
  }

  @Override
  public boolean required() {
    return false;
  }

  @Override
  public PredicateResult predicate(MatcherModel matcherModel) {
    String bParam = matcherModel.get(ModelConstant.BUSINESS_PARAMS);
    if (StringUtils.isEmpty(bParam)) {
      return PredicateResult.success();
    }
    BusinessParamUtil.BusinessParamWrapper businessParamWrapper =
        BusinessParamUtil.parseFromJsonStr(bParam);
    List<BusinessParamUtil.BusinessParam> params = businessParamWrapper.getParams();
    if (params == null || params.isEmpty()) {
      return PredicateResult.fail(getName() + " illegal json");
    }
    if (BusinessParamPatternEnum.getPatternMatcher(businessParamWrapper.getPattern()) == null) {
      return PredicateResult.fail(getName() + " unsupported matching pattern");
    }
    return PredicateResult.success();
  }
}
