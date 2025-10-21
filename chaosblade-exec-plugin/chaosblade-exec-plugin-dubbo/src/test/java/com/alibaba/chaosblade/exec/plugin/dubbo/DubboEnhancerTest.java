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

package com.alibaba.chaosblade.exec.plugin.dubbo;

import com.alibaba.chaosblade.exec.plugin.dubbo.consumer.DubboConsumerEnhancer;
import org.junit.Assert;
import org.junit.Test;

/**
 * 测试dubbo服务url拦截
 *
 * @author dknight.chen
 * @create 2019/6/24 5:21 PM
 */
public class DubboEnhancerTest {

  public class MockDubboUrl {
    private String serviceKey;

    public String getServiceKey() {
      return serviceKey;
    }

    public void setServiceKey(String serviceKey) {
      this.serviceKey = serviceKey;
    }
  }

  @Test
  public void testGetServiceNameWithVersionGroup_ServiceSplit() throws Exception {
    DubboConsumerEnhancer consumerEnhancer = new DubboConsumerEnhancer();
    MockDubboUrl mockDubboUrl = new MockDubboUrl();
    mockDubboUrl.setServiceKey("daily/com.alibaba.dubbo.demo.DemoService");
    String[] serviceVersion =
        consumerEnhancer.getServiceNameWithVersionGroup(new Object(), mockDubboUrl);
    Assert.assertTrue(3 == serviceVersion.length);
    Assert.assertEquals("daily", serviceVersion[2]);
    mockDubboUrl.setServiceKey("com.alibaba.dubbo.demo.DemoService:2.0.0");
    serviceVersion = consumerEnhancer.getServiceNameWithVersionGroup(new Object(), mockDubboUrl);
    Assert.assertTrue(3 == serviceVersion.length);
    Assert.assertEquals("com.alibaba.dubbo.demo.DemoService", serviceVersion[0]);
    Assert.assertEquals("2.0.0", serviceVersion[1]);
    Assert.assertNull(serviceVersion[2]);
  }
}
