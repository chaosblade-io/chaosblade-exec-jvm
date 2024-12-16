package com.alibaba.chaosblade.exec.plugin.kafka;

/**
 * @author ljzhxx@gmail.com
 */
public interface KafkaConstant {

    String TARGET_NAME = "kafka";

    String PRODUCER_KEY = "producer";

    String CONSUMER_KEY = "consumer";

    String TOPIC_KEY = "topic";

    String GROUP_ID_KEY = "groupId";

    String PRODUCER_CLASS = "org.apache.kafka.clients.producer.KafkaProducer";

    String SEND = "send";

    String CONSUMER_CLASS = "org.apache.kafka.clients.consumer.KafkaConsumer";

    String POLL = "poll";

}
