package com.alibaba.chaosblade.exec.plugin.log.model;

import com.alibaba.chaosblade.exec.common.model.FrameworkModelSpec;
import com.alibaba.chaosblade.exec.common.model.action.ActionSpec;
import com.alibaba.chaosblade.exec.common.model.action.delay.DelayActionSpec;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherSpec;
import com.alibaba.chaosblade.exec.plugin.log.LogConstant;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shizhi.zhu@qunar.com
 */
public class LogModelSpec extends FrameworkModelSpec {

    public LogModelSpec() {
        addActionExample();
    }

    private void addActionExample() {
        List<ActionSpec> actions = getActions();
        for (ActionSpec action : actions) {
            if (action instanceof DelayActionSpec) {
                action.setLongDesc("Log framework delay experiment");
                action.setExample("# Do a delay 1s experiment take effect 100 times\n" +
                        "blade create log delay --logback --time 1000 --effect-count 100\n");
            }
        }
    }

    @Override
    public String getShortDesc() {
        return "log experiment";
    }

    @Override
    public String getLongDesc() {
        return "log experiment for testing service delay.";
    }

    @Override
    protected List<MatcherSpec> createNewMatcherSpecs() {
        ArrayList<MatcherSpec> matcherSpecs = new ArrayList<MatcherSpec>();
        matcherSpecs.add(new LogbackModelSpec());
        return matcherSpecs;
    }

    @Override
    public String getTarget() {
        return LogConstant.TARGET;
    }
}
