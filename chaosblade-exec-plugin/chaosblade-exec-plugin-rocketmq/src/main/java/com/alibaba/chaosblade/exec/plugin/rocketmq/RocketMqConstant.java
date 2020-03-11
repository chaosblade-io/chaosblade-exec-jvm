package com.alibaba.chaosblade.exec.plugin.rocketmq;

/**
 * @author RinaisSuper
 * @date 2019-07-23
 * @email rinalhb@icloud.com
 */
public interface RocketMqConstant {

    public static String PLUGIN_NAME = "rocketmq";

    public static String REMOTEING_SUPER_CLASS_ALIBABA = "com.alibaba.rocketmq.remoting.netty.NettyRemotingAbstract";

    public static String REMOTEING_SUPER_CLASS_APACHE = "org.apache.rocketmq.remoting.netty.NettyRemotingAbstract";

    public static String SYNC_INVOKE_METHOD = "invokeSyncImpl";

    public static String ASYNC_INVOKE_METHOD = "invokeAsyncImpl";

    public static String ONEWAY_INVOKE_METHOD = "invokeOnewayImpl";


    public static String FLAG_NAME_TOPIC = "topic";

    public static String FLAG_PRODUCER_GROUP = "producerGroup";

    public static String FLAG_CONSUMER_GROUP = "consumerGroup";

}
