package com.otel.business.c.exec.plugin.http.okhttp3;

import com.otel.business.c.exec.common.aop.Enhancer;
import com.otel.business.c.exec.common.aop.PointCut;
import com.otel.business.c.exec.plugin.http.HttpConstant;
import com.otel.business.c.exec.plugin.http.HttpPlugin;

/**
 * okhttp3 的插件定义
 *
 * @author pengpj
 * @date 2020-7-13
 */
public class Okhttp3Plugin extends HttpPlugin {

  @Override
  public String getName() {
    return HttpConstant.OKHTTP3_TARGET_NAME;
  }

  @Override
  public PointCut getPointCut() {
    return new Okhttp3PointCut();
  }

  @Override
  public Enhancer getEnhancer() {
    return new Okhttp3Enhancer();
  }
}
