package com.otel.business.c.exec.plugin.http.resttemplate;

import com.otel.business.c.exec.common.aop.Enhancer;
import com.otel.business.c.exec.common.aop.PointCut;
import com.otel.business.c.exec.plugin.http.HttpConstant;
import com.otel.business.c.exec.plugin.http.HttpPlugin;

/**
 * @Author yuhan
 *
 * @package: com.otel.business.c.exec.plugin.restTemplate @Date 2019-05-10 10:25
 */
public class RestTemplatePlugin extends HttpPlugin {

  @Override
  public String getName() {
    return HttpConstant.REST_TARGET_NAME;
  }

  @Override
  public PointCut getPointCut() {
    return new RestTemplatePointCut();
  }

  @Override
  public Enhancer getEnhancer() {
    return new RestTemplateEnhancer();
  }
}
