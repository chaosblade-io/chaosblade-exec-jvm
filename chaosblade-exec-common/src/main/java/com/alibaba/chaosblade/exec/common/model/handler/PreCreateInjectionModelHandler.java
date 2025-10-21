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

package com.alibaba.chaosblade.exec.common.model.handler;

import com.alibaba.chaosblade.exec.common.exception.ExperimentException;
import com.alibaba.chaosblade.exec.common.model.Model;

/**
 * Model Level Handler before create injections
 *
 * <p>Useful for need do something on model level not action level when receive a create request
 *
 * <p>For example,a module contains multiple actions, before invoke actions,we need do something,so
 * you can make @{@link com.alibaba.chaosblade.exec.common.model.ModelSpec} implements this
 * interface.
 *
 * @author RinaisSuper
 * @date 2019-04-19
 */
public interface PreCreateInjectionModelHandler {

  /**
   * Invoke before create injections
   *
   * @param suid the injection event id
   * @param model model object
   * @throws ExperimentException throw if anything wrong
   */
  void preCreate(String suid, Model model) throws ExperimentException;
}
