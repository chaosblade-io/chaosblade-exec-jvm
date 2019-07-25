package com.alibaba.chaosblade.exec.plugin.http;

import com.alibaba.chaosblade.exec.common.aop.Plugin;
import com.alibaba.chaosblade.exec.common.model.ModelSpec;
import com.alibaba.chaosblade.exec.plugin.http.model.HttpModelSpec;

/**
 * @Author yuhan
 * @package: com.alibaba.chaosblade.exec.plugin.restTemplate
 * @Date 2019-05-10 10:25
 */
public abstract class HttpPlugin implements Plugin {

    @Override
    public ModelSpec getModelSpec() {
        return new HttpModelSpec();
    }
}
