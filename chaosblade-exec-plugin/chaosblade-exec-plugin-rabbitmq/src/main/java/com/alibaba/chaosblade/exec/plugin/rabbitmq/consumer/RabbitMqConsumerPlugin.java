package com.alibaba.chaosblade.exec.plugin.rabbitmq.consumer;

import com.alibaba.chaosblade.exec.common.aop.Enhancer;
import com.alibaba.chaosblade.exec.common.aop.PointCut;
import com.alibaba.chaosblade.exec.plugin.rabbitmq.RabbitMqPlugin;

/**
 * @author raygenyang@163.com
 */
public class RabbitMqConsumerPlugin extends RabbitMqPlugin {
    @Override
    public String getName() {
        return "consumer";
    }

    @Override
    public PointCut getPointCut() {
        return new RabbitMqConsumerPointCut();
    }

    @Override
    public Enhancer getEnhancer() {
        return new RabbitMqConsumerEnhancer();
    }
}
