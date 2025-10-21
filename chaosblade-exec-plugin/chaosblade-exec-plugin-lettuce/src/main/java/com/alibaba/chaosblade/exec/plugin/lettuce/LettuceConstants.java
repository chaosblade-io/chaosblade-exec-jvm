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

package com.alibaba.chaosblade.exec.plugin.lettuce;

/**
 * @author yefei
 * @create 2020-11-23 14:58
 */
public class LettuceConstants {

  public static final String KEY = "key";
  public static final String VALUE = "value";
  public static final String CMD = "cmd";

  public static final String CLASS = "io.lettuce.core.protocol.CommandHandler";
  public static final String METHOD = "write";
}
