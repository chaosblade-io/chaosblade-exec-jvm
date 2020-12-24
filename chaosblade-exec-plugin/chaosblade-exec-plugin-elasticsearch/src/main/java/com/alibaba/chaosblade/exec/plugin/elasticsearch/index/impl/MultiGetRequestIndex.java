package com.alibaba.chaosblade.exec.plugin.elasticsearch.index.impl;

import com.alibaba.chaosblade.exec.common.util.ReflectUtil;
import com.alibaba.chaosblade.exec.plugin.elasticsearch.index.AbstractRequestIndex;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Yuhan Tang
 * @package: com.alibaba.chaosblade.exec.plugin.elasticsearch.index.impl
 * @Date 2020-10-30 16:16
 */
public class MultiGetRequestIndex extends AbstractRequestIndex {

    @Override
    public String getName() {
        return "multiGet";
    }

    @Override
    public List<String> getIndex0(Object target) throws Exception {
        List<Object> list = ReflectUtil.invokeMethod(target, "getItems");
        List<String> indexes = new ArrayList<String>();
        for (Object item : list) {
            indexes.add(index(item, "index"));
        }
        return indexes;
    }
}
