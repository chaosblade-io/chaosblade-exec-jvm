package com.alibaba.chaosblade.exec.plugin.feign.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.alibaba.chaosblade.exec.common.aop.PredicateResult;
import com.alibaba.chaosblade.exec.common.model.FrameworkModelSpec;
import com.alibaba.chaosblade.exec.common.model.Model;
import com.alibaba.chaosblade.exec.common.model.action.ActionSpec;
import com.alibaba.chaosblade.exec.common.model.action.delay.DelayActionSpec;
import com.alibaba.chaosblade.exec.common.model.action.exception.ThrowCustomExceptionActionSpec;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherModel;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherSpec;
import com.alibaba.chaosblade.exec.plugin.feign.FeignConstant;

/**
 * @author guoyu486@gmail.com
 */
public class FeignModelSpec extends FrameworkModelSpec implements FeignConstant {

    public FeignModelSpec() {
        addActionExample();
    }

    private void addActionExample() {
        List<ActionSpec> actions = getActions();
        for (ActionSpec action : actions) {
            if (action instanceof DelayActionSpec) {
                action.setLongDesc("feign delay experiment");
                action.setExample("# Delay when service call api time\n" +
                    "blade create feign delay --time 3000 --service-name test-service --url /example/feign/api ");
            } else if (action instanceof ThrowCustomExceptionActionSpec) {
                action.setLongDesc("Feign throws custom exception experiment");
                action.setExample("# Throw exception when service call api \n" +
                    "blade create feign throwCustomException --exception java.lang.Exception --exception-message "
                    + "mock-beans-exception --service-name test-service --url /example/feign/api");
            }
        }
    }


    @Override
    protected List<MatcherSpec> createNewMatcherSpecs() {
        ArrayList<MatcherSpec> arrayList = new ArrayList<MatcherSpec>();
        arrayList.add(new ServiceNameMatcherSpec());
        arrayList.add(new UrlMatcherSpec());
        return arrayList;
    }

    @Override
    public String getTarget() {
        return TARGET_NAME;
    }

    @Override
    public String getShortDesc() {
        return "feign experiment";
    }

    @Override
    public String getLongDesc() {
        return "feign experiment for testing service api delay and exception.";
    }

    @Override
    protected PredicateResult preMatcherPredicate(Model model) {
        if (model == null) {
            return PredicateResult.fail("matcher not found for feign");
        }
        MatcherModel matcher = model.getMatcher();
        Set<String> keySet = matcher.getMatchers().keySet();
        if (keySet.contains(SERVICE_NAME) || keySet.contains(TEMPLATE_URL)) {
            return PredicateResult.success();
        } else {
            return PredicateResult.fail("feign chaos require param [serviceName,url] ");
        }
    }
}
