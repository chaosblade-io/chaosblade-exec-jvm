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

package com.alibaba.chaosblade.exec.common.transport;

import com.alibaba.chaosblade.exec.common.util.StringUtil;
import java.util.HashMap;
import java.util.Map;

/** @author Changjun Xiao */
public class Request {
  protected final Map<String, String> headers;
  protected final Map<String, String> params;

  public Request() {
    this.headers = new HashMap<String, String>();
    this.params = new HashMap<String, String>();
  }

  public Map<String, String> getHeaders() {
    return headers;
  }

  public Map<String, String> getParams() {
    return params;
  }

  public String getHeader(String key) {
    return headers.get(key);
  }

  public String getParam(String key) {
    return params.get(key);
  }

  public void removeHeader(String key) {
    headers.remove(key);
  }

  public void removeParam(String key) {
    params.remove(key);
  }

  /**
   * Add header
   *
   * @param key
   * @param value
   * @return
   */
  public Request addHeader(String key, String value) {
    if (StringUtil.isBlank(key)) {
      throw new IllegalArgumentException("Parameter key cannot be empty");
    }
    headers.put(key, value);
    return this;
  }

  /**
   * Add parameter
   *
   * @param key
   * @param value
   * @return
   */
  public Request addParam(String key, String value) {
    if (StringUtil.isBlank(key)) {
      throw new IllegalArgumentException("Parameter key cannot be empty");
    }
    params.put(key, value);
    return this;
  }

  public Request addParams(Map<String, String> params) {
    this.params.putAll(params);
    return this;
  }
}
