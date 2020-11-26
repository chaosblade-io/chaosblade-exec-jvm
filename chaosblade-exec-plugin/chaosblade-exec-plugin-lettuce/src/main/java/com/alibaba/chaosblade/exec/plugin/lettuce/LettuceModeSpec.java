package com.alibaba.chaosblade.exec.plugin.lettuce;

import com.alibaba.chaosblade.exec.common.model.FrameworkModelSpec;
import com.alibaba.chaosblade.exec.common.model.action.ActionSpec;
import com.alibaba.chaosblade.exec.common.model.action.delay.DelayActionSpec;
import com.alibaba.chaosblade.exec.common.model.action.exception.ThrowCustomExceptionActionSpec;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherSpec;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yefei
 */
public class LettuceModeSpec extends FrameworkModelSpec {

    public LettuceModeSpec() {
        addUpdateActionSpec();
        addActionExample();
    }

    private void addUpdateActionSpec() {
        UpdateActionSpec actionSpec = new UpdateActionSpec();
        actionSpec.addMatcherDesc(new LettuceKeyMatcherSpec());
        actionSpec.addMatcherDesc(new LettuceCmdMatcherSpec());
        addActionSpec(actionSpec);
    }

    private void addActionExample() {
        List<ActionSpec> actions = getActions();
        for (ActionSpec action : actions) {
            if (action instanceof DelayActionSpec) {
                action.setLongDesc("Lettuce client delay experiments");
                action.setExample("# Do a delay 2s experiment on Lettuce `hset key name lina` command\n" +
                        "blade create lettuce delay --cmd hset --key name --time 2000\n\n" +

                        "#Do a delay 2s experiment on Jedis `key name lina` command\n" +
                        "blade create lettuce delay --key name --time 2000");
            } else if (action instanceof ThrowCustomExceptionActionSpec) {
                action.setLongDesc("Lettuce client throws custom exception experiments");
                action.setExample("# Do a throws custom exception experiment on Lettuce `key name lina` command\n" +
                        "blade create lettuce throwCustomException --exception java.lang.Exception --key name");
            } else if (action instanceof UpdateActionSpec) {
                action.setLongDesc("Lettuce client update value experiments");
                action.setExample("# Do a update value experiment on Lettuce `key name lina` command\n" +
                        "blade create lettuce update --value \"i'm hacker\" --key name");
            }
        }
    }

    @Override
    public List<MatcherSpec> createNewMatcherSpecs() {
        List<MatcherSpec> matchers = new ArrayList<MatcherSpec>();
        matchers.add(new LettuceKeyMatcherSpec());
        matchers.add(new LettuceCmdMatcherSpec());
        return matchers;
    }

    @Override
    public String getTarget() {
        return "lettuce";
    }

    @Override
    public String getShortDesc() {
        return "redis client lettuce experiment";
    }

    @Override
    public String getLongDesc() {
        return "redis client lettuce experiment";
    }

}