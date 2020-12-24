package com.alibaba.chaosblade.exec.plugin.elasticsearch.index;

import java.util.List;

/**
 * @Author Yuhan Tang
 * @package: com.alibaba.chaosblade.exec.plugin.elasticsearch.index.impl
 * @Date 2020-10-30 16:16
 */
public interface RequestIndex {
    /**
     * 获取名称
     *
     * @return
     */
    String getName();

    /**
     * 获取index名称
     * @param target
     * @return
     */
    List<String> getIndex(Object target) throws Exception;
}
