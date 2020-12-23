package com.alibaba.chaosblade.exec.plugin.lettuce;

import com.alibaba.chaosblade.exec.common.aop.PredicateResult;
import com.alibaba.chaosblade.exec.common.model.FlagSpec;
import com.alibaba.chaosblade.exec.common.model.action.ActionModel;
import com.alibaba.chaosblade.exec.common.model.action.BaseActionSpec;
import com.alibaba.chaosblade.exec.common.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yefei
 */
public class UpdateActionSpec extends BaseActionSpec {

    private final static LettuceValueFlagSpec VALUE_FLAG_SPEC = new LettuceValueFlagSpec();

    public UpdateActionSpec() {
        super(new LettuceActionExecutor(VALUE_FLAG_SPEC));
    }

    @Override
    public String getName() {
        return "update";
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public String getShortDesc() {
        return "update action spec";
    }

    @Override
    public String getLongDesc() {
        return "update action spec";
    }

    @Override
    public List<FlagSpec> getActionFlags() {
        List<FlagSpec> flagSpecs = new ArrayList<FlagSpec>();
        flagSpecs.add(VALUE_FLAG_SPEC);
        return flagSpecs;
    }

    @Override
    public PredicateResult predicate(ActionModel actionModel) {
        if (StringUtil.isBlank(actionModel.getFlag(VALUE_FLAG_SPEC.getName()))) {
            return PredicateResult.fail("less value argument");
        }
        return PredicateResult.success();
    }
}