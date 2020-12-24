package com.alibaba.chaosblade.exec.plugin.elasticsearch;

import com.alibaba.chaosblade.exec.common.model.FrameworkModelSpec;
import com.alibaba.chaosblade.exec.common.model.action.ActionSpec;
import com.alibaba.chaosblade.exec.common.model.action.delay.DelayActionSpec;
import com.alibaba.chaosblade.exec.common.model.action.exception.ThrowCustomExceptionActionSpec;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherSpec;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author <a href="tangyuhan@shulie.io">yuhan.tang</a>
 * @package: com.alibaba.chaosblade.exec.plugin.hbase
 * @Date 2020-10-30 14:10
 */
public class ElasticSearchModelSpec extends FrameworkModelSpec {

    public ElasticSearchModelSpec() {
        addActionExample();
    }

    private void addActionExample() {
        List<ActionSpec> actions = getActions();
        for (ActionSpec action : actions) {
            if (action instanceof DelayActionSpec) {
                action.setLongDesc("ElasticSearch delay experiment");
                action.setExample("# Do a delay 2s experiment for ElasticSearch client\n" +
                        "blade create es delay --index index1,index2");
            }
            if (action instanceof ThrowCustomExceptionActionSpec) {
                action.setLongDesc("ElasticSearch throws customer exception experiment");
                action.setExample("# Do a throws customer exception experiment for ElasticSearch client\n" +
                        "blade create es throwCustomException --exception java.lang.Exception --index index2");
            }
        }
    }

    @Override
    protected List<MatcherSpec> createNewMatcherSpecs() {
        ArrayList<MatcherSpec> matcherSpecs = new ArrayList<MatcherSpec>();
        matcherSpecs.add(new ElasticSearchIndexMatcherSpec());
        return matcherSpecs;
    }

    @Override
    public String getTarget() {
        return ElasticSearchConstant.TARGET_NAME;
    }

    @Override
    public String getShortDesc() {
        return "ElasticSearch experiment!";
    }

    @Override
    public String getLongDesc() {
        return "ElasticSearch experiment contains delay and exception by table name and so on.";
    }
}
