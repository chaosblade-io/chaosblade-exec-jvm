package com.alibaba.xblade.exec.plugin.tars.client;

import com.alibaba.xblade.exec.common.aop.Enhancer;
import com.alibaba.xblade.exec.common.aop.PointCut;
import com.alibaba.xblade.exec.plugin.tars.TarsPlugin;

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
