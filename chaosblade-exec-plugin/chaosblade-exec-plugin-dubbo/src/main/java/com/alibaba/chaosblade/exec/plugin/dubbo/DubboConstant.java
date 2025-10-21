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

package com.alibaba.chaosblade.exec.plugin.dubbo;

import com.alibaba.chaosblade.exec.common.plugin.MethodConstant;

/** @author Changjun Xiao */
public interface DubboConstant {

  String TIMEOUT_KEY = "timeout";
  String TIMEOUT_EXCEPTION_MSG = "from chaosblade mock timeout";

  String VERSION_KEY = "version";
  String APP_KEY = "appname";
  String SERVICE_KEY = "service";
  String METHOD_KEY = MethodConstant.METHOD_MATCHER_NAME;
  String GROUP_KEY = "group";

  String TARGET_NAME = "dubbo";

  String CONSUMER_KEY = "consumer";
  String PROVIDER_KEY = "provider";

  String CALL_POINT_KEY = "call-point";
}
