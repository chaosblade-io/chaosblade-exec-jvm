package com.alibaba.chaosblade.exec.plugin.rocketmq;

import com.alibaba.chaosblade.exec.common.util.ReflectUtil;

/**
 * @author haibin
 * @date 2019-07-24
 * @email haibin.lhb@alibaba-inc.com
 */
public class RocketMqEnhancerTest {

    @org.junit.Test
    public void doBeforeAdvice() {

    }

    public static void main(String[] args) {
        Object rocketMqEnhancerTest = new RocketMqEnhancerTest();
        System.out.println(ReflectUtil
            .isAssignableFrom(Thread.currentThread().getContextClassLoader(), rocketMqEnhancerTest.getClass(),
                RocketMqEnhancerTest.class.getName()));
    }

}