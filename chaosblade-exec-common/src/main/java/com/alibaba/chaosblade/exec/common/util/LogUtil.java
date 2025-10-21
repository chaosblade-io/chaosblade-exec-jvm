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

package com.alibaba.chaosblade.exec.common.util;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** @author Changjun Xiao */
public class LogUtil {

  /** Set log level to debug */
  public static void setDebug() {
    setLogLevel("DEBUG");
  }

  /**
   * Set log level
   *
   * @param level DEBUG
   */
  public static void setLogLevel(String level) {
    ILoggerFactory loggerFactory = LoggerFactory.getILoggerFactory();
    if (loggerFactory instanceof LoggerContext) {
      LoggerContext loggerContext = (LoggerContext) loggerFactory;
      Logger logger = loggerContext.getLogger(Logger.ROOT_LOGGER_NAME);
      ((ch.qos.logback.classic.Logger) logger).setLevel(Level.toLevel(level));
      return;
    }
    throw new IllegalStateException("not support the log context object");
  }

  /** Set log level to info */
  public static void setInfo() {
    setLogLevel("INFO");
  }
}
