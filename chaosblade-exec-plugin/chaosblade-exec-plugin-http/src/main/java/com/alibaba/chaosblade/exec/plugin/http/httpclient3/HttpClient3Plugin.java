package com.alibaba.xblade.exec.plugin.http.httpclient3;

import com.alibaba.xblade.exec.common.aop.Enhancer;
import com.alibaba.xblade.exec.common.aop.PointCut;
import com.alibaba.xblade.exec.plugin.http.HttpConstant;
import com.alibaba.xblade.exec.plugin.http.HttpPlugin;

/**
 * @Author yuhan
 *
 * @package: com.alibaba.xblade.exec.plugin.restTemplate @Date 2019-05-10 10:25
 */
public class HttpClient3Plugin extends HttpPlugin {

  @Override
  public String getName() {
    return HttpConstant.HTTPCLIENT3_TARGET_NAME;
  }

  @Override
  public PointCut getPointCut() {
    return new HttpClient3PointCut();
  }

  @Override
  public Enhancer getEnhancer() {
    return new HttpClient3Enhancer();
  }
}
