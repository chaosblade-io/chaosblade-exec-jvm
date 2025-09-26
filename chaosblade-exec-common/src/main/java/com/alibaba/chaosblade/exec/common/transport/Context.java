/*
 * Copyright 1999-2019 Alibaba Group Holding Ltd.
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

package com.alibaba.chaosblade.exec.common.transport;

import java.util.HashMap;
import java.util.Map;

/** @author Changjun Xiao */
public class Context {
  private Map<String, String> ctx;

  public Context() {
    this.ctx = new HashMap<String, String>();
  }

  public String get(String key) {
    return ctx.get(key);
  }

  public Context set(String key, String value) {
    ctx.put(key, value);
    return this;
  }

  @Override
  public String toString() {
    return "Context{" + "ctx=" + ctx + '}';
  }
}
