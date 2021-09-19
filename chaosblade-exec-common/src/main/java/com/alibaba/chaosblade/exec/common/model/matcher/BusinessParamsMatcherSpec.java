package com.alibaba.chaosblade.exec.common.model.matcher;

import com.alibaba.chaosblade.exec.common.aop.PredicateResult;
import com.alibaba.chaosblade.exec.common.constant.ModelConstant;
import com.alibaba.chaosblade.exec.common.util.BusinessParamUtil;

import java.util.List;

/**
 * @author wufunc@gmail.com
 */
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
        List<BusinessParamUtil.BusinessParam> params = BusinessParamUtil.parseFromJsonStr(bParam);
        if (params == null || params.isEmpty()) {
            return PredicateResult.fail(getName() + " illegal json");
        }
        return PredicateResult.success();
    }
}
