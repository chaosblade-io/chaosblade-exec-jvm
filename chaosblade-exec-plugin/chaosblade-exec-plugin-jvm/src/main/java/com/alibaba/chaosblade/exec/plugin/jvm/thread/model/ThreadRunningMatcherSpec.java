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

package com.alibaba.chaosblade.exec.plugin.jvm.thread.model;

import com.alibaba.chaosblade.exec.common.model.matcher.BasePredicateMatcherSpec;
import com.alibaba.chaosblade.exec.plugin.jvm.JvmConstant;

/**
 * @Author qianfan
 *
 * @package: com.alibaba.chaosblade.exec.plugin.jvm.thread @Date 2020/12/22 12:56 下午
 */
public class ThreadRunningMatcherSpec extends BasePredicateMatcherSpec {

  @Override
  public String getName() {
    return JvmConstant.ACTION_THREAD_RUNNING;
  }

  @Override
  public String getDesc() {
    return "To tag threads running experiment.";
  }

  @Override
  public boolean noArgs() {
    return true;
  }

  @Override
  public boolean required() {
    return false;
  }
}
