package com.alibaba.chaosblade.exec.plugin.rabbitmq;

/**
 * @author raygenyang@163.com
 */
public interface RabbitMqConstant {

    String TARGET_NAME = "rabbitmq";

    String PRODUCER_KEY = "producer";

    String CONSUMER_KEY = "consumer";

    String EXCHANGE_KEY = "exchange";

    String ROUTING_KEY = "routingkey";

    String TOPIC_KEY = "topic";

    String PUBLISH_METHOD = "basicPublish";

    String CHANNELN_CLASS = "com.rabbitmq.client.impl.ChannelN";

    String CONSUMER_CLASS = "com.rabbitmq.client.Consumer";

    String DELIVERY_METHOD = "handleDelivery";

    String GET_EXCHANGE_METHOD = "getExchange";

    String GET_ROUTING_KEY_METHOD = "getRoutingKey";

}
