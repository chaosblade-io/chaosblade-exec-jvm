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
import com.alibaba.chaosblade.exec.common.util.ModelUtil;
import com.alibaba.chaosblade.exec.common.util.StringUtil;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** @author Changjun Xiao */
public class DefaultStatusManager implements StatusManager {

  private static final Logger LOGGER = LoggerFactory.getLogger(DefaultStatusManager.class);

  /** The enhancer matched for the application */
  private ConcurrentHashMap<String, Boolean> enhancers = new ConcurrentHashMap<String, Boolean>();
  /** The experiment rules */
  private ConcurrentHashMap<String, ConcurrentHashMap<String, StatusMetric>> models =
      new ConcurrentHashMap<String, ConcurrentHashMap<String, StatusMetric>>();

  /** suid, modelUid */
  private ConcurrentHashMap<String, String> experiments = new ConcurrentHashMap<String, String>();

  private volatile boolean closed;

  @Override
  public void registerEnhancer(String enhancerName) {
    if (isClosed()) {
      return;
    }
    Boolean oldValue = enhancers.putIfAbsent(enhancerName, Boolean.TRUE);
    if (oldValue == null) {
      LOGGER.info("register enhancer: " + enhancerName);
    }
  }

  @Override
  public RegisterResult registerExp(String suid, Model model) {

    // get the target metric map
    ConcurrentHashMap<String, StatusMetric> metricMap = getMetricMap(model.getTarget());
    // create identifier by model
    String identifier = ModelUtil.getIdentifier(model);
    // check identifier exists or not
    StatusMetric metric = metricMap.putIfAbsent(identifier, new StatusMetric(model));
    if (metric != null) {
      LOGGER.warn(model.toString() + " exists");
      return RegisterResult.fail(metric.getModel());
    }
    experiments.put(suid, identifier);
    return RegisterResult.success();
  }

  @Override
  public Model removeExp(String suid) {
    // get model identifier
    String identifier = experiments.remove(suid);
    if (StringUtil.isBlank(identifier)) {
      return null;
    }
    String target = ModelUtil.getTarget(identifier);
    ConcurrentHashMap<String, StatusMetric> metricMap = models.get(target);
    if (metricMap == null || metricMap.size() == 0) {
      return null;
    }
    // remove identifier
    StatusMetric metric = metricMap.remove(identifier);
    if (metric == null) {
      return null;
    }
    return metric.getModel();
  }

  private ConcurrentHashMap<String, StatusMetric> getMetricMap(String targetName) {
    ConcurrentHashMap<String, StatusMetric> metricMap = models.get(targetName);
    if (metricMap == null) {
      metricMap = new ConcurrentHashMap<String, StatusMetric>();
    }
    ConcurrentHashMap<String, StatusMetric> oldMetricMap =
        models.putIfAbsent(targetName, metricMap);
    return oldMetricMap == null ? metricMap : oldMetricMap;
  }

  @Override
  public Map<String, List<StatusMetric>> listExps() {
    HashMap<String, List<StatusMetric>> map = new HashMap<String, List<StatusMetric>>();
    Set<Entry<String, ConcurrentHashMap<String, StatusMetric>>> entries = models.entrySet();
    for (Entry<String, ConcurrentHashMap<String, StatusMetric>> entry : entries) {
      ConcurrentHashMap<String, StatusMetric> metricMap = entry.getValue();
      String targetName = entry.getKey();
      List<StatusMetric> statusMetrics = map.get(targetName);
      if (statusMetrics == null) {
        statusMetrics = new LinkedList<StatusMetric>();
        map.put(targetName, statusMetrics);
      }
      for (StatusMetric statusMetric : metricMap.values()) {
        statusMetrics.add(statusMetric);
      }
    }
    return map;
  }

  @Override
  public List<StatusMetric> getExpByTarget(String targetName) {
    ConcurrentHashMap<String, StatusMetric> metricMap = models.get(targetName);
    if (metricMap == null) {
      return Collections.emptyList();
    }
    LinkedList<StatusMetric> statusMetrics = new LinkedList<StatusMetric>();
    for (StatusMetric statusMetric : metricMap.values()) {
      statusMetrics.add(statusMetric);
    }
    return statusMetrics;
  }

  @Override
  public boolean expExists(String targetName) {
    return models.containsKey(targetName) && models.get(targetName).size() > 0;
  }

  @Override
  public StatusMetric getStatusMetricByUid(String uid) {
    // get model identifier
    String identifier = experiments.get(uid);
    if (StringUtil.isBlank(identifier)) {
      return null;
    }
    String target = ModelUtil.getTarget(identifier);
    ConcurrentHashMap<String, StatusMetric> metricMap = models.get(target);
    if (metricMap == null || metricMap.size() == 0) {
      return null;
    }
    return metricMap.get(identifier);
  }

  @Override
  public Set<String> getAllUids() {
    Enumeration<String> keys = experiments.keys();
    HashSet<String> uids = new HashSet<String>();
    while (keys.hasMoreElements()) {
      uids.add(keys.nextElement());
    }
    return uids;
  }

  @Override
  public Set<String> listUids(String target, String action) {
    HashSet<String> uids = new HashSet<String>();
    // identifier
    ConcurrentHashMap<String, StatusMetric> metricMap = models.get(target);
    if (metricMap == null) {
      return uids;
    }
    HashSet<String> identifiers = new HashSet<String>();
    Set<Entry<String, StatusMetric>> entries = metricMap.entrySet();
    for (Entry<String, StatusMetric> entry : entries) {
      identifiers.add(entry.getKey());
    }
    Set<Entry<String, String>> uidIdentifierEntries = experiments.entrySet();
    for (Entry<String, String> entry : uidIdentifierEntries) {
      if (identifiers.contains(entry.getValue())) {
        uids.add(entry.getKey());
      }
    }
    return uids;
  }

  @Override
  public void load() {
    closed = false;
  }

  @Override
  public void unload() {
    closed = true;
    experiments.clear();
    models.clear();
    enhancers.clear();
  }

  public boolean isClosed() {
    return closed;
  }
}
