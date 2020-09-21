package com.alibaba.chaosblade.exec.plugin.druid;

import com.alibaba.chaosblade.exec.common.aop.PredicateResult;
import com.alibaba.chaosblade.exec.common.exception.ExperimentException;
import com.alibaba.chaosblade.exec.common.model.BaseModelSpec;
import com.alibaba.chaosblade.exec.common.model.Model;
import com.alibaba.chaosblade.exec.common.model.action.ActionExecutor;
import com.alibaba.chaosblade.exec.common.model.action.ActionSpec;
import com.alibaba.chaosblade.exec.common.model.action.connpool.ConnectionPoolFullActionSpec;
import com.alibaba.chaosblade.exec.common.model.handler.PreCreateInjectionModelHandler;
import com.alibaba.chaosblade.exec.common.model.handler.PreDestroyInjectionModelHandler;

/**
 * @author Changjun Xiao
 */
public class DruidModelSpec extends BaseModelSpec implements PreCreateInjectionModelHandler,
        PreDestroyInjectionModelHandler {

    public DruidModelSpec() {
        super();
        addConnectionPoolFullAction();
    }

    @Override
    public String getTarget() {
        return DruidConstant.TARGET_NAME;
    }

    @Override
    public String getShortDesc() {
        return "Experiment with the Druid";
    }

    @Override
    public String getLongDesc() {
        return "Experiment with the Druid database connection pool, For example `blade create druid connectionpoolfull`";
    }

    @Override
    protected PredicateResult preMatcherPredicate(Model matcherSpecs) {
        return PredicateResult.success();
    }

    @Override
    public void preCreate(String suid, Model model) throws ExperimentException {
        if (ConnectionPoolFullActionSpec.NAME.equals(model.getActionName())) {
            ActionSpec actionSpec = getActionSpec(model.getActionName());
            ActionExecutor actionExecutor = actionSpec.getActionExecutor();
            if (actionExecutor instanceof DruidConnectionPoolFullExecutor) {
                DruidConnectionPoolFullExecutor executor = (DruidConnectionPoolFullExecutor) actionExecutor;
                executor.setExpReceived(true);
            } else {
                throw new ExperimentException("The executor about connection pool full for tddl is error when "
                        + "creating");
            }
        }
    }

    @Override
    public void preDestroy(String suid, Model model) throws ExperimentException {
        if (ConnectionPoolFullActionSpec.NAME.equals(model.getActionName())) {
            ActionSpec actionSpec = getActionSpec(model.getActionName());
            ActionExecutor actionExecutor = actionSpec.getActionExecutor();
            if (actionExecutor instanceof DruidConnectionPoolFullExecutor) {
                DruidConnectionPoolFullExecutor executor = (DruidConnectionPoolFullExecutor)actionExecutor;
                executor.revoke();
            } else {
                throw new ExperimentException("The executor about connection pool full for tddl is error when "
                    + "destroying");
            }
        }
    }

    private void addConnectionPoolFullAction() {
        ConnectionPoolFullActionSpec actionSpec = new ConnectionPoolFullActionSpec(DruidConnectionPoolFullExecutor.INSTANCE);
        actionSpec.setExample("# Do a full load experiment on the Druid database connection pool\n" +
                "blade create druid connectionpoolfull");
        addActionSpec(actionSpec);
    }
}
