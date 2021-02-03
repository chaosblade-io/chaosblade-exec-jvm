package com.alibaba.chaosblade.exec.plugin.kafka.producer;

import com.alibaba.chaosblade.exec.common.aop.Enhancer;
import com.alibaba.chaosblade.exec.common.aop.PointCut;
import com.alibaba.chaosblade.exec.plugin.kafka.KafkaPlugin;

/**
 * @author ljzhxx@gmail.com
 */
public class KafkaProducerPlugin extends KafkaPlugin {
    @Override
    public String getName() {
        return "producer";
    }

    @Override
    public PointCut getPointCut() {
        return new KafkaProducerPointCut();
    }

    @Override
    public Enhancer getEnhancer() {
        return new KafkaProducerEnhancer();
    }
}
