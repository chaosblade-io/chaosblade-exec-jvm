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
import com.alibaba.chaosblade.exec.plugin.jvm.oom.JvmMemoryArea;
import java.util.Arrays;

/**
 * @author RinaisSuper
 * @date 2019-04-18
 * @email rinalhb@icloud.com
 */
public class JvmMemoryAreaFlagSpec implements FlagSpec {

  @Override
  public String getName() {
    return JvmConstant.FLAG_NAME_MEMORY_AREA;
  }

  @Override
  public String getDesc() {
    return "Jvm memory area you want to cause OutOfMemoryError,the options:"
        + Arrays.toString(JvmMemoryArea.getAreaNames());
  }

  @Override
  public boolean noArgs() {
    return false;
  }

  @Override
  public boolean required() {
    return true;
  }
}
