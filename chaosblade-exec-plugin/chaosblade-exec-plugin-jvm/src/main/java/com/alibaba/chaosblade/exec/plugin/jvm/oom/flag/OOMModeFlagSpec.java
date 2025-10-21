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

package com.alibaba.chaosblade.exec.plugin.jvm.oom.flag;

import com.alibaba.chaosblade.exec.common.model.FlagSpec;
import com.alibaba.chaosblade.exec.plugin.jvm.JvmConstant;

/**
 * @author haibin
 * @date 2019-04-23
 */
public class OOMModeFlagSpec implements FlagSpec {
  @Override
  public String getName() {
    return JvmConstant.FLAG_NAME_OOM_HAPPEN_MODE;
  }

  @Override
  public String getDesc() {
    return "Decide oom happen mode in wild-mode or not,default is false,if true,will quickly generate oom error,and "
        + "the memory  "
        + "will not "
        + "release until stop,if false,the oom error will not generate at once,and if oom happens frequently,"
        + "the  memory will release each once";
  }

  @Override
  public boolean noArgs() {
    return false;
  }

  @Override
  public boolean required() {
    return false;
  }
}
