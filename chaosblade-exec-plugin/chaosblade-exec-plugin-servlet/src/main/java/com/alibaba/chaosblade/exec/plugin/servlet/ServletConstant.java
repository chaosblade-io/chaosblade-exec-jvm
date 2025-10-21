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

package com.alibaba.chaosblade.exec.plugin.servlet;

/** @author Changjun Xiao */
public interface ServletConstant {

  String QUERY_STRING_KEY = "querystring";

  String QUERY_STRING_REGEX_PATTERN_KEY = "querystring-regex-pattern";

  String METHOD_KEY = "method";

  String REQUEST_PATH_KEY = "requestpath";

  String REQUEST_PATH_REGEX_PATTERN_KEY = "requestpath-regex-pattern";

  String TARGET_NAME = "servlet";

  String EQUALS_SYMBOL = "=";

  String AND_SYMBOL = "&";

  String CONTENT_TYPE_JSON = "application/json";

  /** ----------- servlet request method --------* */
  String GET_REQUEST_URI = "getRequestURI";

  String GET_METHOD = "getMethod";

  String GET_PARAMETER_MAP = "getParameterMap";

  String GET_QUERY_STRING = "getQueryString";

  String GET_HEADER = "getHeader";
  /** ----------- servlet request method --------* */
}
