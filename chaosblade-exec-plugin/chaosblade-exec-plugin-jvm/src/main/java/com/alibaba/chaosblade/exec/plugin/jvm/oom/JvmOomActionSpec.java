package com.alibaba.chaosblade.exec.plugin.jvm.oom;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.chaosblade.exec.common.aop.PredicateResult;
import com.alibaba.chaosblade.exec.common.model.FlagSpec;
import com.alibaba.chaosblade.exec.common.model.action.ActionModel;
import com.alibaba.chaosblade.exec.common.model.action.BaseActionSpec;
import com.alibaba.chaosblade.exec.common.util.StringUtils;
import com.alibaba.chaosblade.exec.plugin.jvm.JvmConstant;
import com.alibaba.chaosblade.exec.plugin.jvm.oom.executor.JvmOomExecutorFacade;
import com.alibaba.chaosblade.exec.plugin.jvm.oom.flag.JvmMemoryAreaFlagSpec;

/**
 * @author haibin
 * @date 2019-04-18
 * @email haibin.lhb@alibaba-inc.com
 */
public class JvmOomActionSpec extends BaseActionSpec {

    public JvmOomActionSpec() {
        super(new JvmOomExecutorFacade());
    }

    @Override
    public String getName() {
        return JvmConstant.ACTION_OUT_OF_MEMORY_ERROR_NAME;
    }

    @Override
    public String[] getAliases() {
        return new String[] {JvmConstant.ACTION_OUT_OF_MEMORY_ERROR_ALIAS};
    }

    @Override
    public String getShortDesc() {
        return "";
    }

    @Override
    public String getLongDesc() {
        return null;
    }

    @Override
    public List<FlagSpec> getActionFlags() {
        List<FlagSpec> flagSpecs = new ArrayList<FlagSpec>();
        flagSpecs.add(new JvmMemoryAreaFlagSpec());
        return flagSpecs;
    }

    @Override
    public PredicateResult predicate(ActionModel actionModel) {
        String area = actionModel.getFlag(JvmConstant.FLAG_NAME_MEMORY_AREA);
        if (StringUtils.isEmpty(area)) {
            return PredicateResult.fail("missed argument:area");
        }
        return PredicateResult.success();
    }
}
