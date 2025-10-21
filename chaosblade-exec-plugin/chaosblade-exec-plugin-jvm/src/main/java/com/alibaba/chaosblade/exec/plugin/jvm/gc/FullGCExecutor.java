/*
 * Copyright 2025 The ChaosBlade Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alibaba.chaosblade.exec.plugin.jvm.gc;

import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.common.util.ConfigUtil;
import com.alibaba.chaosblade.exec.plugin.jvm.JvmConstant;
import com.alibaba.chaosblade.exec.plugin.jvm.StoppableActionExecutor;
import java.lang.management.ManagementFactory;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import javax.management.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** @author shizhi.zhu@qunar.com */
public class FullGCExecutor implements StoppableActionExecutor {

  private static final Logger LOGGER = LoggerFactory.getLogger(FullGCExecutor.class);

  private volatile ScheduledExecutorService scheduledExecutorService;
  private AtomicInteger fullGCCounter = new AtomicInteger();
  private final AtomicBoolean started = new AtomicBoolean(false);

  private final MBeanServer mbeanServer = ManagementFactory.getPlatformMBeanServer();

  @Override
  public synchronized void run(EnhancerModel enhancerModel) throws Exception {
    if (started.compareAndSet(false, true)) {
      final int interval =
          ConfigUtil.getActionFlag(enhancerModel, JvmConstant.FLAG_FULL_GC_INTERVAL, 1);
      final int totalCount =
          ConfigUtil.getActionFlag(enhancerModel, JvmConstant.FLAG_FULL_GC_TOTAL_COUNT, 0);
      scheduledExecutorService =
          Executors.newSingleThreadScheduledExecutor(
              new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                  return new Thread(r, "chaosblade-fgc-thread");
                }
              });
      scheduledExecutorService.scheduleAtFixedRate(
          new Runnable() {
            @Override
            public void run() {
              try {
                if (totalCount > 0 && fullGCCounter.get() >= totalCount) {
                  doStop();
                  return;
                }
                doGc();
              } catch (Exception e) {
                LOGGER.error("do gcClassHistogram error", e);
              }
            }
          },
          0,
          interval <= 0 ? 1 : interval,
          TimeUnit.MILLISECONDS);
    } else {
      LOGGER.warn("another executor is running now");
    }
  }

  @Override
  public synchronized void stop(EnhancerModel enhancerModel) throws Exception {
    doStop();
  }

  private void doStop() {
    if (started.compareAndSet(true, false)) {
      if (scheduledExecutorService != null && !scheduledExecutorService.isShutdown()) {
        try {
          scheduledExecutorService.shutdownNow();
        } catch (Exception e) {
          LOGGER.error("shutdown executor error", e);
        }
        scheduledExecutorService = null;
        fullGCCounter = new AtomicInteger();
        LOGGER.info("jvm full gc stopped");
      }
    }
  }

  private void doGc()
      throws MalformedObjectNameException, IntrospectionException, ReflectionException,
          InstanceNotFoundException, MBeanException {
    if (jvmBefore8()) {
      doGCHistogramInSeparateProcess();
    } else {
      doGcHistogramInMbean();
    }
  }

  private void doGcHistogramInMbean()
      throws MalformedObjectNameException, IntrospectionException, InstanceNotFoundException,
          ReflectionException, MBeanException {
    Set<ObjectName> objectNames =
        mbeanServer.queryNames(new ObjectName("com.sun.management:type=DiagnosticCommand"), null);
    if (objectNames == null || objectNames.isEmpty()) {
      LOGGER.warn("no mBean found, exit");
      return;
    }
    for (ObjectName name : objectNames) {
      MBeanInfo mBeanInfo = mbeanServer.getMBeanInfo(name);
      MBeanOperationInfo[] operations = mBeanInfo.getOperations();
      for (MBeanOperationInfo op : operations) {
        if (op.getName().equals("gcClassHistogram")) {
          String[] emptyStringArgs = {};
          Object[] dcmdArgs = {emptyStringArgs};
          String[] signature = {String[].class.getName()};
          Object invoke = mbeanServer.invoke(name, op.getName(), dcmdArgs, signature);
          int count = fullGCCounter.getAndIncrement();
          LOGGER.debug("do gc class histogram count:{} \n, {}", count, invoke);
        }
      }
    }
  }

  private void doGCHistogramInSeparateProcess() {
    int pid = getPid();
    String cmd = null;
    try {
      String javaHome = System.getenv("JAVA_HOME");
      if (javaHome.endsWith("/")) {
        javaHome = javaHome.substring(0, javaHome.length() - 1);
      }
      cmd = javaHome + "/bin/jmap";
      ProcessBuilder pb = new ProcessBuilder(cmd, "-histo:live", String.valueOf(pid));
      Process start = pb.start();
      int count = fullGCCounter.getAndIncrement();
      start.waitFor();
      LOGGER.debug("do gc class histogram in separate process count:{} cmd:{}", count, cmd);
    } catch (Exception e) {
      LOGGER.error("exec jmap error, cmd:{}", cmd, e);
    }
  }

  private static int getPid() {
    byte pid = 0;
    try {
      String name = ManagementFactory.getRuntimeMXBean().getName();
      return Integer.parseInt(name.substring(0, name.indexOf('@')));
    } catch (Throwable e) {
      return pid;
    }
  }

  private boolean jvmBefore8() {
    String javaVersion = System.getProperty("java.version");
    String[] versions = javaVersion.split("\\.");
    if (Integer.parseInt(versions[1]) < 8 && Integer.parseInt(versions[0]) == 1) {
      return true;
    }
    return false;
  }
}
