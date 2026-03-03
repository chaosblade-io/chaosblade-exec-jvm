package com.alibaba.xblade.exec.plugin.tars;

import com.alibaba.xblade.exec.common.aop.Plugin;
import com.alibaba.xblade.exec.common.model.ModelSpec;
import com.alibaba.xblade.exec.plugin.tars.model.TarsModelSpec;

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
