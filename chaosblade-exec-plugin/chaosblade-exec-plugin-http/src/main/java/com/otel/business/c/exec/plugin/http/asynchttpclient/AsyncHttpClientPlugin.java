package com.otel.business.c.exec.plugin.http.asynchttpclient;

import com.otel.business.c.exec.common.aop.Enhancer;
import com.otel.business.c.exec.common.aop.PointCut;
import com.otel.business.c.exec.plugin.http.HttpConstant;
import com.otel.business.c.exec.plugin.http.HttpPlugin;

/** @author shizhi.zhu@qunar.com */
public class AsyncHttpClientPlugin extends HttpPlugin {
  @Override
  public String getName() {
    return HttpConstant.ASYNC_HTTP_TARGET_NAME;
  }

  @Override
  public PointCut getPointCut() {
    return new AsyncHttpClientPointCut();
  }

  @Override
  public Enhancer getEnhancer() {
    return new AsyncHttpClientEnhancerWrapper();
  }
}
