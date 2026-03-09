package com.otel.business.c.exec.plugin.http.httpclient4;

import com.otel.business.c.exec.common.aop.Enhancer;
import com.otel.business.c.exec.common.aop.PointCut;
import com.otel.business.c.exec.plugin.http.HttpConstant;
import com.otel.business.c.exec.plugin.http.HttpPlugin;

/**
 * @Author yuhan
 *
 * @package: com.otel.business.c.exec.plugin.restTemplate @Date 2019-05-10 10:25
 */
public class HttpClient4Plugin extends HttpPlugin {

  @Override
  public String getName() {
    return HttpConstant.HTTPCLIENT4_TARGET_NAME;
  }

  @Override
  public PointCut getPointCut() {
    return new HttpClient4PointCut();
  }

  @Override
  public Enhancer getEnhancer() {
    return new HttpClient4Enhancer();
  }
}
