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

package com.alibaba.chaosblade.exec.common.util;

import com.alibaba.chaosblade.exec.common.center.ManagerFactory;
import com.alibaba.chaosblade.exec.common.center.StatusMetric;
import com.alibaba.chaosblade.exec.common.constant.ModelConstant;
import com.alibaba.chaosblade.exec.common.context.GlobalContext;
import com.alibaba.chaosblade.exec.common.model.Model;
import com.alibaba.chaosblade.exec.spi.BusinessDataGetter;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** @author wufunc@gmail.com */
public class BusinessParamUtil {
  private static final Logger LOGGER = LoggerFactory.getLogger(BusinessParamUtil.class);
  private static GlobalContext businessParamContent = new GlobalContext(3600, 100000);

  private static Map<String, BusinessParamWrapper> getAndParse(String target) throws Exception {
    Map<String, BusinessParamWrapper> paramsMap = new HashMap<String, BusinessParamWrapper>();
    List<StatusMetric> statusMetrics = ManagerFactory.getStatusManager().getExpByTarget(target);
    for (StatusMetric statusMetric : statusMetrics) {
      Model model = statusMetric.getModel();
      String flag = model.getMatcher().get(ModelConstant.BUSINESS_PARAMS);
      BusinessParamWrapper businessParamWrapper = parseFromJsonStr(flag);
      paramsMap.put(ModelUtil.getIdentifier(model), businessParamWrapper);
    }
    return paramsMap;
  }

  public static BusinessParamWrapper parseFromJsonStr(String str) {
    try {
      if (!StringUtil.isBlank(str)) {
        if (businessParamContent.containsKey(str)) {
          return (BusinessParamWrapper) businessParamContent.get(str);
        }
        BusinessParamWrapper businessParamWrapper =
            JsonUtil.reader().readValue(str, BusinessParamWrapper.class);
        businessParamContent.put(str, businessParamWrapper);
        return businessParamWrapper;
      }
    } catch (Exception exp) {
      LOGGER.warn("{} is not illegal json ", ModelConstant.BUSINESS_PARAMS);
    }
    return new BusinessParamWrapper();
  }

  public static Map<String, Map<String, String>> getAndParse(
      String target, BusinessDataGetter dataGetter) throws Exception {
    Map<String, BusinessParamWrapper> businessParams = getAndParse(target);
    Map<String, Map<String, String>> result = new HashMap<String, Map<String, String>>();
    for (Map.Entry<String, BusinessParamWrapper> entry : businessParams.entrySet()) {
      if (entry.getValue() == null || entry.getValue().getParams() == null) {
        continue;
      }
      result.put(entry.getKey(), new HashMap<String, String>());
      for (BusinessParam businessParam : entry.getValue().getParams()) {
        String value = "";
        if (businessParam.getMode().equals(mode.rpc.getValue())) {
          value = getValueFromRpc(businessParam, dataGetter);
        } else {
          value = getValueFromSPI(businessParam, dataGetter);
        }
        if (!StringUtils.isEmpty(value)) {
          result.get(entry.getKey()).put(businessParam.getKey(), value);
        }
      }
    }
    return result;
  }

  private static String getValueFromSPI(
      BusinessParam businessParam, BusinessDataGetter dataGetter) {
    List<Object> objects =
        ManagerFactory.spiServiceManager()
            .getServices(
                BusinessDataGetter.class.getName(), Thread.currentThread().getContextClassLoader());
    if (objects == null || objects.isEmpty()) {
      return null;
    }
    for (Object object : objects) {
      String tmpValue = null;
      try {
        tmpValue =
            ReflectUtil.invokeMethod(object, "get", new Object[] {businessParam.getKey()}, true);
      } catch (Exception e) {
        LOGGER.warn(
            "get business value from spi class error,class :{}", object.getClass().getName(), e);
      }
      if (!StringUtils.isEmpty(tmpValue)) {
        return tmpValue;
      }
    }
    return null;
  }

  private static String getValueFromRpc(
      BusinessParam businessParam, BusinessDataGetter dataGetter) {
    String value = "";
    try {
      if (businessParam.hasMultiLevelKey()) {
        String json = dataGetter.get(businessParam.getFirstLevelKey());
        if (StringUtils.isEmpty(json)) {
          return null;
        }
        JsonNode jsonNode = JsonUtil.reader().readTree(json);
        JsonNode nodeResult = jsonNode.findValue(businessParam.getJsonPath());
        if ((nodeResult != null)) {
          value = nodeResult.asText();
        }
      } else {
        value = dataGetter.get(businessParam.getKey());
      }
    } catch (Exception e) {
      LOGGER.warn("get business data from rpc error", e);
    }
    return value;
  }

  public static class BusinessParamWrapper {
    private String pattern;
    private List<BusinessParamUtil.BusinessParam> params;

    public String getPattern() {
      return pattern;
    }

    public void setPattern(String pattern) {
      this.pattern = pattern;
    }

    public List<BusinessParam> getParams() {
      return params;
    }

    public void setParams(List<BusinessParam> params) {
      this.params = params;
    }
  }

  public static class BusinessParam {
    private String key;
    private String value;
    private String mode = BusinessParamUtil.mode.rpc.getValue();

    public String getFirstLevelKey() {
      if (hasMultiLevelKey()) {
        return key.substring(0, key.indexOf("."));
      } else {
        return key;
      }
    }

    public boolean hasMultiLevelKey() {
      if (key.contains(".")) {
        return true;
      } else {
        return false;
      }
    }

    public String getJsonPath() {
      return key.substring(key.indexOf(".") + 1, key.length());
    }

    public String getKey() {
      return key;
    }

    public void setKey(String key) {
      this.key = key;
    }

    public String getValue() {
      return value;
    }

    public void setValue(String value) {
      this.value = value;
    }

    public String getMode() {
      return mode;
    }

    public void setMode(String mode) {
      this.mode = mode;
    }
  }

  private enum mode {
    spi("spi"),
    rpc("rpc");
    private String value;

    mode(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }
  }
}
