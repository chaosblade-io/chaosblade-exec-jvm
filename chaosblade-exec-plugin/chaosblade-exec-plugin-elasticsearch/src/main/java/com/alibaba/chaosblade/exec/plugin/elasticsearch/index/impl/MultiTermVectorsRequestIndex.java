package com.alibaba.chaosblade.exec.plugin.elasticsearch.index.impl;

import com.alibaba.chaosblade.exec.common.util.ReflectUtil;
import com.alibaba.chaosblade.exec.plugin.elasticsearch.index.AbstractRequestIndex;
import org.elasticsearch.action.termvectors.MultiTermVectorsRequest;

import java.util.List;

/**
 * @Author Yuhan Tang
 * @package: com.alibaba.chaosblade.exec.plugin.elasticsearch.index.impl
 * @Date 2020-10-30 16:16
 */
public class MultiTermVectorsRequestIndex extends AbstractRequestIndex {

    @Override
    public String getName() {
        return "multiTermVectors";
    }

    @Override
    public List<String> getIndex0(Object target) throws Exception {
        List list = ReflectUtil.invokeMethod(target, "getRequests");
        return indexList(list);
    }
}
