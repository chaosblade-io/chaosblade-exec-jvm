package com.alibaba.chaosblade.exec.plugin.rocketmq;

import java.lang.reflect.Method;

import com.alibaba.chaosblade.exec.common.aop.BeforeEnhancer;
import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherModel;
import com.alibaba.chaosblade.exec.common.util.ReflectUtil;

/**
 * @author RinaisSuper
 * @date 2019-07-24
 * @email haibin.lhb@alibaba-inc.com
 */
public class RocketMqEnhancer extends BeforeEnhancer implements RocketMqConstant {

    private static String FIELD_CUSTOM_HEADER = "customHeader";

    public static String CLASS_PULL_MESSAGE_REQUEST_HEADER
        = "com.alibaba.rocketmq.common.protocol.header.PullMessageRequestHeader";

    public static String CLASS_SEND_MESSAGE_REQUEST_HEADER
        = "com.alibaba.rocketmq.common.protocol.header.SendMessageRequestHeader";

    public static String CLASS_SEND_MESSAGE_REQUEST_HEADERV2
        = "com.alibaba.rocketmq.common.protocol.header.SendMessageRequestHeaderV2";

    public static String CLASS_REMOTEING_COMMAND_CLASS = "com.alibaba.rocketmq.remoting.protocol.RemotingCommand";

    @Override
    public EnhancerModel doBeforeAdvice(ClassLoader classLoader, String className, Object object, Method method,
        Object[] methodArguments) throws Exception {
        MatcherModel matcherModel = new MatcherModel();
        Object remoteingCommnadRequest = selectParamByClassName(classLoader, methodArguments,
            CLASS_REMOTEING_COMMAND_CLASS);
        if (remoteingCommnadRequest == null) {
            return new EnhancerModel(classLoader, matcherModel);
        } else {
            Object header = ReflectUtil.getFieldValue(remoteingCommnadRequest, FIELD_CUSTOM_HEADER, false);
            if (header == null) {
                return new EnhancerModel(classLoader, matcherModel);
            }
            String topic = null;
            String consumerGroup = null;
            String producerGroup = null;
            if (isPullMessageHeader(classLoader, header)) {
                topic = ReflectUtil.getFieldValue(header, FLAG_NAME_TOPIC, false);
                consumerGroup = ReflectUtil.getFieldValue(header, FLAG_CONSUMER_GROUP, false);
            } else if (isSendMessageHeader(classLoader, header)) {
                topic = ReflectUtil.getFieldValue(header, FLAG_NAME_TOPIC, false);
                producerGroup = ReflectUtil.getFieldValue(header, FLAG_PRODUCER_GROUP, false);
            } else if (isSendMessageHeaderV2(classLoader, header)) {
                topic = ReflectUtil.getFieldValue(header, "b", false);
                producerGroup = ReflectUtil.getFieldValue(header, "a", false);
            }
            matcherModel.add(FLAG_NAME_TOPIC, topic);
            matcherModel.add(FLAG_CONSUMER_GROUP, consumerGroup);
            matcherModel.add(FLAG_PRODUCER_GROUP, producerGroup);
            return new EnhancerModel(classLoader, matcherModel);
        }
    }

    private boolean isSendMessageHeader(ClassLoader classLoader, Object header) {
        return ReflectUtil.isAssignableFrom(classLoader, header.getClass(), CLASS_SEND_MESSAGE_REQUEST_HEADER);
    }

    private boolean isSendMessageHeaderV2(ClassLoader classLoader, Object header) {
        return ReflectUtil.isAssignableFrom(classLoader, header.getClass(), CLASS_SEND_MESSAGE_REQUEST_HEADERV2);
    }

    private boolean isPullMessageHeader(ClassLoader classLoader, Object header) {
        return ReflectUtil.isAssignableFrom(classLoader, header.getClass(), CLASS_PULL_MESSAGE_REQUEST_HEADER);
    }

    private Object selectParamByClassName(ClassLoader classLoader, Object[] methodArguments, String filterClassName) {
        for (Object object : methodArguments) {
            if (ReflectUtil.isAssignableFrom(classLoader, object.getClass(), filterClassName)) {
                return object;
            }
        }
        return null;
    }

}
