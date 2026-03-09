package com.otel.business.c.exec.plugin.hbase;

import com.otel.business.c.exec.common.aop.Enhancer;
import com.otel.business.c.exec.common.aop.Plugin;
import com.otel.business.c.exec.common.aop.PointCut;
import com.otel.business.c.exec.common.model.ModelSpec;

/**
 * @Author <a href="tangyuhan@shulie.io">yuhan.tang</a>
 *
 * @package: com.otel.business.c.exec.plugin.hbase @Date 2020-10-30 14:06
 */
public class HbasePlugin implements Plugin {

  @Override
  public String getName() {
    return HbaseConstant.TARGET_NAME;
  }

  @Override
  public ModelSpec getModelSpec() {
    return new HbaseModelSpec();
  }

  @Override
  public PointCut getPointCut() {
    return new HbasePointCut();
  }

  @Override
  public Enhancer getEnhancer() {
    return new HbaseEnhancer();
  }
}
