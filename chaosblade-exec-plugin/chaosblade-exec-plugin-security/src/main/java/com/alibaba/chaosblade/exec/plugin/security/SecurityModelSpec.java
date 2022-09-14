package com.alibaba.chaosblade.exec.plugin.security;

import com.alibaba.chaosblade.exec.common.model.FrameworkModelSpec;
import com.alibaba.chaosblade.exec.common.model.action.ActionSpec;
import com.alibaba.chaosblade.exec.common.model.action.delay.DelayActionSpec;
import com.alibaba.chaosblade.exec.common.model.action.exception.ThrowCustomExceptionActionSpec;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherSpec;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liubin@njzfit.cn
 */
public class SecurityModelSpec extends FrameworkModelSpec {

    public SecurityModelSpec() {
        addActionExample();
    }

    private void addActionExample() {
        List<ActionSpec> actions = getActions();
        for (ActionSpec action : actions) {
            if (action instanceof DelayActionSpec) {
                action.setLongDesc("SpringSecurity delay experiment");
                action.setExample("# Do a delay 2s experiment for SpringSecurity login operation\n"
                        + "blade create security delay --username admin --time 2000\n\n");
            }
            if (action instanceof ThrowCustomExceptionActionSpec) {
                action.setLongDesc("SpringSecurity throws customer exception experiment");
                action.setExample("# Do a throws customer exception experiment for SpringSecurity login\n" +
                        "blade create security throwCustomException --exception java.lang.Exception --username admin");
            }
        }
    }

    @Override
    protected List<MatcherSpec> createNewMatcherSpecs() {
        ArrayList<MatcherSpec> matcherSpecs = new ArrayList<MatcherSpec>();
        matcherSpecs.add(new SecurityMatcherSpec());
        return matcherSpecs;
    }

    @Override
    public String getTarget() {
        return SecurityConstant.PLUGIN_NAME;
    }

    @Override
    public String getShortDesc() {
        return "SpringSecurity login experiment";
    }

    @Override
    public String getLongDesc() {
        return "SpringSecurity login experiment contains delay and exception by command and so on";
    }
}
