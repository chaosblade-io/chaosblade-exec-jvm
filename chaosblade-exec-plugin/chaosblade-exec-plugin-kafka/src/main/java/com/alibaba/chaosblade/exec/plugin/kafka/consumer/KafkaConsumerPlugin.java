package com.alibaba.chaosblade.exec.plugin.kafka.consumer;

import com.alibaba.chaosblade.exec.common.aop.Enhancer;
import com.alibaba.chaosblade.exec.common.aop.PointCut;
import com.alibaba.chaosblade.exec.plugin.kafka.KafkaPlugin;

/**
 * @author ljzhxx@gmail.com
 */
public class KafkaConsumerPlugin extends KafkaPlugin {
    @Override
    public String getName() {
        return "consumer";
    }

    @Override
    public PointCut getPointCut() {
        return new KafkaConsumerPointCut();
    }

    @Override
    public Enhancer getEnhancer() {
        return new KafkaConsumerEnhancer();
    }
}
