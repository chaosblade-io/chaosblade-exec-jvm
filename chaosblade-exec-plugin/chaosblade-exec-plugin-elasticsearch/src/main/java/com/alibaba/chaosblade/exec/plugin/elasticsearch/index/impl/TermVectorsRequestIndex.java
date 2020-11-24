package com.alibaba.chaosblade.exec.plugin.elasticsearch.index.impl;

import com.alibaba.chaosblade.exec.plugin.elasticsearch.index.AbstractRequestIndex;
import org.elasticsearch.action.termvectors.TermVectorsRequest;

import java.util.Arrays;
import java.util.List;

/**
 * @Author Yuhan Tang
 * @package: com.alibaba.chaosblade.exec.plugin.elasticsearch.index.impl
 * @Date 2020-10-30 16:16
 */
public class TermVectorsRequestIndex extends AbstractRequestIndex {

    @Override
    public String getName() {
        return "termVectors";
    }

    @Override
    public List<String> getIndex0(Object target) throws Exception {
        return Arrays.asList(index(target, "index"));
    }
}
