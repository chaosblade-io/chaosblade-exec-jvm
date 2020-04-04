package com.alibaba.chaosblade.exec.plugin.tars.model;


import com.alibaba.chaosblade.exec.common.model.matcher.BasePredicateMatcherSpec;
import com.alibaba.chaosblade.exec.plugin.tars.TarsConstant;

/**
 * @author saikei
 * @email lishiji@huya.com
 */
public class ServantNameMatcherSpec extends BasePredicateMatcherSpec {

    @Override
    public String getName() {
        return TarsConstant.SERVANT_NAME;
    }

    @Override
    public String getDesc() {
        return "The name of servant";
    }

    @Override
    public boolean noArgs() {
        return false;
    }

    @Override
    public boolean required() {
        return true;
    }
}
