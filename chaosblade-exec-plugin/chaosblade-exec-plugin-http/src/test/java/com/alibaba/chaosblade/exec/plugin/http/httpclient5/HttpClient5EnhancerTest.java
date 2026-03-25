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

package com.alibaba.chaosblade.exec.plugin.http.httpclient5;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.plugin.http.HttpConstant;
import java.lang.reflect.Method;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.protocol.BasicHttpContext;
import org.apache.hc.core5.http.protocol.HttpContext;
import org.apache.hc.core5.util.Timeout;
import org.junit.Test;

/** Tests for HttpClient5 enhancer. */
public class HttpClient5EnhancerTest {

  private final HttpClient5Enhancer enhancer = new HttpClient5Enhancer();

  @Test
  public void shouldExtractUrlWithoutQueryParameters() throws Exception {
    CloseableHttpClient client = HttpClients.custom().build();
    try {
      HttpGet request = new HttpGet("https://www.taobao.com/search?q=chaosblade");
      String url = enhancer.getUrl(client, new Object[] {null, request, null});
      assertEquals("https://www.taobao.com/search", url);
    } finally {
      client.close();
    }
  }

  @Test
  public void shouldPreferRequestLevelTimeouts() throws Exception {
    CloseableHttpClient client = HttpClients.custom().build();
    try {
      HttpGet request = new HttpGet("https://www.taobao.com");
      request.setConfig(
          RequestConfig.custom()
              .setConnectTimeout(Timeout.ofMilliseconds(1200))
              .setResponseTimeout(Timeout.ofMilliseconds(2300))
              .build());

      int timeout = enhancer.getTimeout(client, new Object[] {null, request, null});
      assertEquals(3500, timeout);
    } finally {
      client.close();
    }
  }

  @Test
  public void shouldFallbackToClientDefaultConfig() throws Exception {
    CloseableHttpClient client =
        HttpClients.custom()
            .setDefaultRequestConfig(
                RequestConfig.custom()
                    .setConnectTimeout(Timeout.ofMilliseconds(800))
                    .setResponseTimeout(Timeout.ofMilliseconds(900))
                    .build())
            .build();
    try {
      HttpGet request = new HttpGet("https://www.taobao.com");
      int timeout = enhancer.getTimeout(client, new Object[] {null, request, null});
      assertEquals(1700, timeout);
    } finally {
      client.close();
    }
  }

  @Test
  public void shouldBuildEnhancerModelForHttpClient5() throws Exception {
    CloseableHttpClient client =
        HttpClients.custom()
            .setDefaultRequestConfig(
                RequestConfig.custom()
                    .setConnectTimeout(Timeout.ofMilliseconds(1000))
                    .setResponseTimeout(Timeout.ofMilliseconds(2000))
                    .build())
            .build();
    try {
      HttpGet request = new HttpGet("https://www.taobao.com/resource?id=1");
      Method method =
          CloseableHttpClient.class.getMethod(
              "execute", HttpHost.class, ClassicHttpRequest.class, HttpContext.class);
      EnhancerModel enhancerModel =
          enhancer.doBeforeAdvice(
              Thread.currentThread().getContextClassLoader(),
              client.getClass().getName(),
              client,
              method,
              new Object[] {
                new HttpHost("https", "www.taobao.com"), request, new BasicHttpContext()
              });

      assertNotNull(enhancerModel);
      assertEquals(
          "https://www.taobao.com/resource",
          enhancerModel.getMatcherModel().get(HttpConstant.URI_KEY));
      assertEquals("true", enhancerModel.getMatcherModel().get(HttpConstant.HTTPCLIENT5));
      assertNotNull(enhancerModel.getTimeoutExecutor());
    } finally {
      client.close();
    }
  }
}
