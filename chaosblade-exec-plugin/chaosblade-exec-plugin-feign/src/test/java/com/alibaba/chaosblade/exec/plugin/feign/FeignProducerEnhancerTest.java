/*
 * Copyright 2025 The ChaosBlade Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alibaba.chaosblade.exec.plugin.feign;

import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import java.lang.reflect.Method;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author hengyu
 * @version 1.0.0
 * @className FeignProducerEnhancerTest
 * @createTime 2022/4/21 14:46:00
 * @description FeignProducerEnhancer测试类
 */
public class FeignProducerEnhancerTest {

  private FeignProducerEnhancer enhancer;
  private Method testMethod;

  @Before
  public void setUp() throws NoSuchMethodException {
    enhancer = new FeignProducerEnhancer();
    // 使用一个真实的Method对象
    testMethod = String.class.getMethod("toString");
  }

  @Test
  public void testDoBeforeAdviceWithNullParameters() throws Exception {
    // 测试空参数情况
    EnhancerModel result =
        enhancer.doBeforeAdvice(
            Thread.currentThread().getContextClassLoader(),
            "com.example.TestClass",
            null,
            testMethod,
            null);

    Assert.assertNull("当参数为null时应该返回null", result);
  }

  @Test
  public void testDoBeforeAdviceWithNullObject() throws Exception {
    // 测试object为null的情况
    Object[] methodArguments = new Object[] {new Object()};

    EnhancerModel result =
        enhancer.doBeforeAdvice(
            Thread.currentThread().getContextClassLoader(),
            "com.example.TestClass",
            null,
            testMethod,
            methodArguments);

    Assert.assertNull("当object为null时应该返回null", result);
  }

  @Test
  public void testDoBeforeAdviceWithNullMethodArguments() throws Exception {
    // 测试methodArguments为null的情况
    Object mockObject = new Object();

    EnhancerModel result =
        enhancer.doBeforeAdvice(
            Thread.currentThread().getContextClassLoader(),
            "com.example.TestClass",
            mockObject,
            testMethod,
            null);

    Assert.assertNull("当methodArguments为null时应该返回null", result);
  }

  @Test
  public void testEnhancerInstance() {
    // 测试增强器实例化
    Assert.assertNotNull("FeignProducerEnhancer实例应该不为null", enhancer);
    Assert.assertTrue(
        "应该继承自BeforeEnhancer",
        enhancer instanceof com.alibaba.chaosblade.exec.common.aop.BeforeEnhancer);
  }
}
