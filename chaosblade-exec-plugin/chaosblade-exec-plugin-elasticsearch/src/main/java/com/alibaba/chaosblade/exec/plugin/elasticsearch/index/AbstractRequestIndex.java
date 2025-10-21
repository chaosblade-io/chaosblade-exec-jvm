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

package com.alibaba.chaosblade.exec.plugin.elasticsearch.index;

import com.alibaba.chaosblade.exec.common.util.ReflectUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractRequestIndex implements RequestIndex {

  private static final Logger logger = LoggerFactory.getLogger(AbstractRequestIndex.class);

  @Override
  public String getName() {
    return "abstract";
  }

  @Override
  public List<String> getIndex(Object target) throws Exception {
    return getIndex0(target);
  }

  public String getIndexOfString(Object target) {
    try {
      List<String> lists = getIndex0(target);
      if (lists.size() < 1) {
        return null;
      }
      StringBuilder stringBuilder = new StringBuilder();
      for (int i = 0; i < lists.size(); i++) {
        stringBuilder.append(lists.get(i));
        if (i < lists.size() - 1) {
          stringBuilder.append(",");
        }
      }
      return stringBuilder.toString();
    } catch (Exception e) {
      logger.error("elasticsearch plugin error : " + e.getMessage(), e);
      return null;
    }
  }

  public abstract List<String> getIndex0(Object target) throws Exception;

  protected List<String> indexList(List list) {
    List<String> indexes = new ArrayList<String>();
    try {
      for (Object req : list) {
        if (req == null) {
          continue;
        }
        RequestIndex requestIndex = RequestIndexProvider.get(req);
        if (requestIndex != null) {
          indexes.addAll(requestIndex.getIndex(req));
        }
      }
    } catch (Exception e) {
      logger.error("elasticsearch plugin error : " + e.getMessage(), e);
    }
    return indexes;
  }

  public String index(Object target, String methodName) throws Exception {
    return ReflectUtil.invokeMethod(target, methodName);
  }

  public List<String> indexs(Object target, String methodName) throws Exception {
    String[] strings = ReflectUtil.invokeMethod(target, methodName);
    return Arrays.asList(strings);
  }
}
