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

package com.alibaba.chaosblade.exec.common.constant;

/** @author Changjun Xiao */
public interface ModelConstant {
  String JVM_TARGET = "jvm";

  String HTTP_TARGET = "http";

  String HTTP_URL_MATCHER_NAME = "uri";

  /** The name of effect percent matcher */
  String EFFECT_PERCENT_MATCHER_NAME = "effect-percent";

  /** The name of effect count matcher */
  String EFFECT_COUNT_MATCHER_NAME = "effect-count";

  /** The flag of regex pattern */
  String REGEX_PATTERN_FLAG = "regex-pattern";

  /** the flag of buisness params */
  String BUSINESS_PARAMS = "b-params";
}
