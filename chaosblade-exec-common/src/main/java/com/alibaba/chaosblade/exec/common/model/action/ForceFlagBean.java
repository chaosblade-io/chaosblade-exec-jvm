/**
 * @Description: TODO
 * @author : yangguangyue
 * @date Date : 2019-08-09 11:47
 */

package com.alibaba.chaosblade.exec.common.model.action;

import com.alibaba.chaosblade.exec.common.model.FlagSpec;

public class ForceFlagBean implements FlagSpec {

    @Override
    public String getName() {
        return "force";
    }

    @Override
    public String getDesc() {
        return "Force update if it exists";
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
