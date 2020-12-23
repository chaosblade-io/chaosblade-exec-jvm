package com.alibaba.chaosblade.exec.plugin.hbase;

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
public class HbaseModelSpec extends FrameworkModelSpec {

    public HbaseModelSpec() {
        addActionExample();
    }

    private void addActionExample() {
        List<ActionSpec> actions = getActions();
        for (ActionSpec action : actions) {
            if (action instanceof DelayActionSpec) {
                action.setLongDesc("hbase delay experiment");
                action.setExample("# Do a delay 2s experiment for hbase client connection INSERT statement\n" +
                        "blade create hbase delay --table table1");
            }
            if (action instanceof ThrowCustomExceptionActionSpec) {
                action.setLongDesc("hbase throws customer exception experiment");
                action.setExample("# Do a throws customer exception experiment for mysql client connection port=3306 INSERT statement\n" +
                        "blade create hbase throwCustomException --exception java.lang.Exception --table table2");
            }
        }
    }

    @Override
    protected List<MatcherSpec> createNewMatcherSpecs() {
        ArrayList<MatcherSpec> matcherSpecs = new ArrayList<MatcherSpec>();
        matcherSpecs.add(new HbaseTableMatcherSpec());
        return matcherSpecs;
    }

    @Override
    public String getTarget() {
        return HbaseConstant.TARGET_NAME;
    }

    @Override
    public String getShortDesc() {
        return "hbase experiment!";
    }

    @Override
    public String getLongDesc() {
        return "hbase experiment contains delay and exception by table name and so on.";
    }
}
