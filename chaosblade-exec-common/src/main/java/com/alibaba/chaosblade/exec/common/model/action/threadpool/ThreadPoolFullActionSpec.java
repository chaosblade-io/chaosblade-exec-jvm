package com.alibaba.chaosblade.exec.common.model.action.threadpool;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.chaosblade.exec.common.aop.PredicateResult;
import com.alibaba.chaosblade.exec.common.model.FlagSpec;
import com.alibaba.chaosblade.exec.common.model.action.ActionExecutor;
import com.alibaba.chaosblade.exec.common.model.action.ActionModel;
import com.alibaba.chaosblade.exec.common.model.action.BaseActionSpec;
import com.alibaba.chaosblade.exec.common.util.StringUtils;

/**
 * @author Changjun Xiao
 */
public class ThreadPoolFullActionSpec extends BaseActionSpec {

    public static final String NAME = "threadpoolfull";

    public ThreadPoolFullActionSpec() {
        super(new DefaultThreadPoolFullExecutor());
    }

    public ThreadPoolFullActionSpec(ActionExecutor actionExecutor) {
        super(actionExecutor);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String[] getAliases() {
        return new String[] {"tpf"};
    }

    @Override
    public String getShortDesc() {
        return "Thread pool full";
    }

    @Override
    public String getLongDesc() {
        if (StringUtils.isNotBlank(super.getLongDesc())) {
            return super.getLongDesc();
        }
        return "Thread pool full";
    }

    @Override
    public List<FlagSpec> getActionFlags() {
        return new ArrayList<FlagSpec>();
    }

    @Override
    public PredicateResult predicate(ActionModel actionModel) {
        return PredicateResult.success();
    }
}
