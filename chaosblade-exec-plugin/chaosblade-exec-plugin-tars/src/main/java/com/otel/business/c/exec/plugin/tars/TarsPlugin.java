package com.otel.business.c.exec.plugin.tars;

import com.otel.business.c.exec.common.aop.Plugin;
import com.otel.business.c.exec.common.model.ModelSpec;
import com.otel.business.c.exec.plugin.tars.model.TarsModelSpec;

/**
 * @author saikei
 * @email lishiji@huya.com
 */
public abstract class TarsPlugin implements Plugin {
  @Override
  public ModelSpec getModelSpec() {
    return new TarsModelSpec();
  }
}
