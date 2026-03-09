package com.otel.business.c.exec.plugin.http;

import com.otel.business.c.exec.common.aop.Plugin;
import com.otel.business.c.exec.common.model.ModelSpec;
import com.otel.business.c.exec.plugin.http.model.HttpModelSpec;

/**
 * @Author yuhan
 *
 * @package: com.otel.business.c.exec.plugin.restTemplate @Date 2019-05-10 10:25
 */
public abstract class HttpPlugin implements Plugin {

  @Override
  public ModelSpec getModelSpec() {
    return new HttpModelSpec();
  }
}
