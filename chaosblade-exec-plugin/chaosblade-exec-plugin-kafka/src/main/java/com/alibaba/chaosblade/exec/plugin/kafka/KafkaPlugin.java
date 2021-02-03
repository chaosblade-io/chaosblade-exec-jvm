package com.alibaba.chaosblade.exec.plugin.kafka;

import com.alibaba.chaosblade.exec.common.aop.Plugin;
import com.alibaba.chaosblade.exec.common.model.ModelSpec;
import com.alibaba.chaosblade.exec.plugin.kafka.model.KafkaModelSpec;

/**
 * @author ljzhxx@gmail.com
 */
public abstract class KafkaPlugin implements Plugin, KafkaConstant {

    @Override
    public ModelSpec getModelSpec() {
        return new KafkaModelSpec();
    }

}
