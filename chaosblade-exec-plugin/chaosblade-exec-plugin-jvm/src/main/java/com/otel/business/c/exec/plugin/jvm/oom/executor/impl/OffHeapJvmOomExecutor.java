package com.otel.business.c.exec.plugin.jvm.oom.executor.impl;

import com.otel.business.c.exec.common.aop.EnhancerModel;
import com.otel.business.c.exec.plugin.jvm.JvmConstant;
import com.otel.business.c.exec.plugin.jvm.oom.JvmMemoryArea;
import com.otel.business.c.exec.plugin.jvm.oom.executor.JvmOomExecutor;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * @author RinaisSuper
 * @date 2019-04-18
 * @email rinalhb@icloud.com
 */
public class OffHeapJvmOomExecutor extends JvmOomExecutor {

  @Override
  public JvmMemoryArea supportArea() {
    return JvmMemoryArea.OFFHEAP;
  }

  private List<ByteBuffer> oomObjects = new ArrayList<ByteBuffer>();

  @Override
  protected void innerRun(EnhancerModel enhancerModel, JvmOomConfiguration jvmOomConfiguration) {
    oomObjects.add(ByteBuffer.allocateDirect(jvmOomConfiguration.getBlock() * JvmConstant.ONE_MB));
  }

  @Override
  public void run(EnhancerModel enhancerModel) throws Exception {
    oomObjects = new ArrayList<ByteBuffer>();
    super.run(enhancerModel);
  }

  @Override
  protected void recycleMemory() {
    this.oomObjects = new ArrayList<ByteBuffer>();
  }

  @Override
  protected void innerStop(EnhancerModel enhancerModel) {
    this.oomObjects = null;
  }
}
