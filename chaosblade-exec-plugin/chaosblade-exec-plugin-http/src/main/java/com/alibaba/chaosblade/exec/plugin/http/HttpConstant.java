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

package com.alibaba.chaosblade.exec.plugin.http;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author yuhan
 *
 * @package: com.alibaba.chaosblade.exec.plugin.http @Date 2019-05-22 16:28
 */
public class HttpConstant {

  public static final String TARGET_NAME = "http";
  public static final String URI_KEY = "uri";
  public static final String REST_KEY = "rest";
  public static final String HTTPCLIENT4 = "httpclient4";
  public static final String HTTPCLIENT3 = "httpclient3";
  public static final String OKHTTP3 = "okhttp3";
  public static final String ASYNC_HTTP_TARGET_NAME = "asyncHttpClient";
  public static final String getURI = "getURI";
  public static final String CALL_POINT_KEY = "call-point";

  public static final Map<String, Method> methodMap = new HashMap<String, Method>();

  public static final int DEFAULT_TIMEOUT = 60000;

  public static final String REQUEST_ID = "c_request_id";

  public static final String REST_TARGET_NAME = "rest";
  public static final String HTTPCLIENT3_TARGET_NAME = "httpclient3";
  public static final String HTTPCLIENT4_TARGET_NAME = "httpclient4";
  public static final String OKHTTP3_TARGET_NAME = "okhttp3";
}
