package com.alibaba.chaosblade.exec.plugin.rabbitmq.producer;

import com.alibaba.chaosblade.exec.common.aop.Enhancer;
import com.alibaba.chaosblade.exec.common.aop.PointCut;
import com.alibaba.chaosblade.exec.plugin.rabbitmq.RabbitMqPlugin;

/**
 * @author raygenyang@163.com
 */
public class RabbitMqProducerPlugin extends RabbitMqPlugin {
    @Override
    public String getName() {
        return "producer";
    }

    @Override
    public PointCut getPointCut() {
        return new RabbitMqProducerPointCut();
    }

    @Override
    public Enhancer getEnhancer() {
        return new RabbitMqProducerEnhancer();
    }
}
