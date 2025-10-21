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

package com.alibaba.chaosblade.exec.bootstrap.jvmsandbox;

import com.alibaba.chaosblade.exec.common.aop.Plugin;
import com.alibaba.chaosblade.exec.common.exception.InterruptProcessException;
import com.alibaba.chaosblade.exec.common.exception.InterruptProcessException.State;
import com.alibaba.chaosblade.exec.common.util.ReflectUtil;
import com.alibaba.jvm.sandbox.api.ProcessControlException;
import com.alibaba.jvm.sandbox.api.event.BeforeEvent;
import com.alibaba.jvm.sandbox.api.event.Event;
import com.alibaba.jvm.sandbox.api.event.ReturnEvent;
import com.alibaba.jvm.sandbox.api.listener.EventListener;
import java.lang.reflect.Method;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** @author Changjun Xiao */
public class AfterEventListener implements EventListener {
  private static final Logger LOGGER = LoggerFactory.getLogger(AfterEventListener.class);

  private Plugin plugin;
  private ThreadLocal<BeforeEventBean> beforeEventCache;

  public AfterEventListener(Plugin plugin) {
    this.plugin = plugin;
    this.beforeEventCache = new ThreadLocal<BeforeEventBean>();
  }

  @Override
  public void onEvent(Event event) throws Throwable {
    if (event instanceof BeforeEvent) {
      BeforeEvent beforeEvent = (BeforeEvent) event;
      BeforeEventBean beforeEventBean =
          new BeforeEventBean(
              beforeEvent.javaClassLoader,
              beforeEvent.target,
              beforeEvent.javaClassName,
              beforeEvent.javaMethodName,
              beforeEvent.javaMethodDesc,
              beforeEvent.argumentArray);
      beforeEventCache.set(beforeEventBean);
      return;
    }
    if (!(event instanceof ReturnEvent)) {
      beforeEventCache.remove();
      return;
    }
    BeforeEventBean beforeEventBean = beforeEventCache.get();
    if (beforeEventBean == null) {
      return;
    }
    ReturnEvent returnEvent = (ReturnEvent) event;
    // get the method class
    Class clazz;
    if (beforeEventBean.getObject() == null) {
      clazz = beforeEventBean.getClassLoader().loadClass(beforeEventBean.getClassName());
    } else {
      clazz = beforeEventBean.getObject().getClass();
    }
    Method method;
    try {
      method =
          ReflectUtil.getMethod(
              clazz, beforeEventBean.getMethodDesc(), beforeEventBean.getMethodName());
    } catch (NoSuchMethodException e) {
      LOGGER.warn(
          "get method by reflection exception for after event. "
              + "class: {}, method: {}, arguments: {}, desc: {}",
          clazz,
          beforeEventBean.getMethodName(),
          Arrays.toString(beforeEventBean.getArgumentArray()),
          beforeEventBean.getMethodDesc(),
          e);
      beforeEventCache.remove();
      return;
    }

    try {
      // do enhancer
      plugin
          .getEnhancer()
          .afterAdvice(
              plugin.getModelSpec().getTarget(),
              beforeEventBean.getClassLoader(),
              beforeEventBean.getClassName(),
              beforeEventBean.getObject(),
              method,
              beforeEventBean.getArgumentArray(),
              returnEvent.object);
    } catch (Exception e) {
      // handle return or throw exception
      if (e instanceof InterruptProcessException) {
        InterruptProcessException exception = (InterruptProcessException) e;
        if (exception.getState() == State.RETURN_IMMEDIATELY) {
          ProcessControlException.throwReturnImmediately(exception.getResponse());
        } else if (exception.getState() == State.THROWS_IMMEDIATELY) {
          ProcessControlException.throwThrowsImmediately((Throwable) exception.getResponse());
        }
      } else {
        throw e;
      }
    } finally {
      beforeEventCache.remove();
    }
  }
}
