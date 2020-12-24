package com.alibaba.chaosblade.exec.plugin.elasticsearch;

import com.alibaba.chaosblade.exec.common.model.matcher.BasePredicateMatcherSpec;

/**
 * @Author <a href="tangyuhan@shulie.io">yuhan.tang</a>
 * @package: com.alibaba.chaosblade.exec.plugin.hbase
 * @Date 2020-10-30 14:21
 */
public class ElasticSearchIndexMatcherSpec extends BasePredicateMatcherSpec {

    @Override
    public String getName() {
        return ElasticSearchConstant.INDEX;
    }

    @Override
    public String getDesc() {
        return "The elasticsearch index which used,many index : index1,index2";
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
