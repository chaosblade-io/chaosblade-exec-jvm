package com.otel.business.c.exec.plugin.tars.client;

import com.otel.business.c.exec.common.aop.Enhancer;
import com.otel.business.c.exec.common.aop.PointCut;
import com.otel.business.c.exec.plugin.tars.TarsPlugin;

/**
 * @author saikei
 * @email lishiji@huya.com
 */
public class TarsClientPlugin extends TarsPlugin {
  @Override
  public String getName() {
    return "client";
  }

  @Override
  public PointCut getPointCut() {
    return new TarsClientPointCut();
  }

  @Override
  public Enhancer getEnhancer() {
    return new TarsClientEnhancer();
  }
}
