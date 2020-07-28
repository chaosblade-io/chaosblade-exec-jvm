package com.alibaba.chaosblade.exec.plugin.rabbitmq;

import com.alibaba.chaosblade.exec.common.aop.Plugin;
import com.alibaba.chaosblade.exec.common.model.ModelSpec;
import com.alibaba.chaosblade.exec.plugin.rabbitmq.model.RabbitMqModelSpec;

/**
 * @author raygenyang@163.com
 */
public abstract class RabbitMqPlugin implements Plugin, RabbitMqConstant {

    @Override
    public ModelSpec getModelSpec() {
        return new RabbitMqModelSpec();
    }

}
