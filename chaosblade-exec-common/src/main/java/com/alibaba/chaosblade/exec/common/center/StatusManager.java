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

package com.alibaba.chaosblade.exec.common.center;

import com.alibaba.chaosblade.exec.common.model.Model;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** @author Changjun Xiao */
public interface StatusManager extends ManagerService {

  /**
   * Register method enhancer name
   *
   * @param enhancerName
   */
  void registerEnhancer(String enhancerName);

  /**
   * Register the experiment rule
   *
   * @param model
   * @return
   */
  RegisterResult registerExp(String uid, Model model);

  /**
   * Remove the experiment by exp uid
   *
   * @param uid
   * @return
   */
  Model removeExp(String uid);

  /**
   * List all experiments
   *
   * @return
   */
  Map<String, List<StatusMetric>> listExps();

  /**
   * List experiments by the exp target
   *
   * @param targetName
   * @return
   */
  List<StatusMetric> getExpByTarget(String targetName);

  /**
   * Assert the target experiment exist or not
   *
   * @param targetName
   * @return
   */
  boolean expExists(String targetName);

  /**
   * Get status metric by uid
   *
   * @param uid
   * @return
   */
  StatusMetric getStatusMetricByUid(String uid);

  /** @return all running experiment uids */
  Set<String> getAllUids();

  /**
   * Search uids by target and action
   *
   * @param target
   * @param action
   * @return
   */
  Set<String> listUids(String target, String action);
}
