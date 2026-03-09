package com.otel.business.c.exec.plugin.hbase;

import com.otel.business.c.exec.common.model.matcher.BasePredicateMatcherSpec;

/**
 * @Author <a href="tangyuhan@shulie.io">yuhan.tang</a>
 *
 * @package: com.otel.business.c.exec.plugin.hbase @Date 2020-10-30 14:21
 */
public class HbaseTableMatcherSpec extends BasePredicateMatcherSpec {

  @Override
  public String getName() {
    return HbaseConstant.TABLE;
  }

  @Override
  public String getDesc() {
    return "The hbase table which used";
  }

  @Override
  public boolean noArgs() {
    return false;
  }

  @Override
  public boolean required() {
    return false;
  }
}
