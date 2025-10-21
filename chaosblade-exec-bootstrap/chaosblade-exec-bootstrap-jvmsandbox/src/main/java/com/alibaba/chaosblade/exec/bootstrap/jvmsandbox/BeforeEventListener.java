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
import com.alibaba.jvm.sandbox.api.listener.EventListener;
import java.lang.reflect.Method;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** @author Changjun Xiao */
public class BeforeEventListener implements EventListener {
  private static final Logger LOGGER = LoggerFactory.getLogger(BeforeEventListener.class);

  private Plugin plugin;

  public BeforeEventListener(Plugin plugin) {
    this.plugin = plugin;
  }

  @Override
  public void onEvent(Event event) throws Throwable {
    if (event instanceof BeforeEvent) {
      handleEvent((BeforeEvent) event);
    }
  }

  /**
   * handle method enter event
   *
   * @param event
   */
  private void handleEvent(BeforeEvent event) throws Throwable {
    Class<?> clazz;
    // get the method class
    if (event.target == null) {
      clazz = event.javaClassLoader.loadClass(event.javaClassName);
    } else {
      clazz = event.target.getClass();
    }
    Method method;
    try {
      method = ReflectUtil.getMethod(clazz, event.javaMethodDesc, event.javaMethodName);
    } catch (NoSuchMethodException e) {
      LOGGER.warn(
          "get method by reflection exception. class: {}, method: {}, arguments: {}, desc: {}",
          event.javaClassName,
          event.javaMethodName,
          Arrays.toString(event.argumentArray),
          event.javaMethodDesc,
          e);
      return;
    }
    try {
      // do enhancer
      plugin
          .getEnhancer()
          .beforeAdvice(
              plugin.getModelSpec().getTarget(),
              event.javaClassLoader,
              event.javaClassName,
              event.target,
              method,
              event.argumentArray);
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
    }
  }
}
