package com.alibaba.chaosblade.exec.plugin.tars.model;

import com.alibaba.chaosblade.exec.common.model.matcher.BasePredicateMatcherSpec;
import com.alibaba.chaosblade.exec.plugin.tars.TarsConstant;

/**
 * @author saikei
 * @email lishiji@huya.com
 */
public class ServantMatcherSpec extends BasePredicateMatcherSpec {

    @Override
    public String getName() {
        return TarsConstant.SERVANT;
    }

    @Override
    public String getDesc() {
        return "to tag servant role experiment";
    }

    @Override
    public boolean noArgs() {
        return true;
    }

    @Override
    public boolean required() {
        return false;
    }
}
