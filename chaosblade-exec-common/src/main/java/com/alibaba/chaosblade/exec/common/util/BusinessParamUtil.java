package com.alibaba.chaosblade.exec.common.util;

import com.alibaba.chaosblade.exec.common.center.ManagerFactory;
import com.alibaba.chaosblade.exec.common.center.StatusMetric;
import com.alibaba.chaosblade.exec.common.constant.ModelConstant;
import com.alibaba.chaosblade.exec.common.context.GlobalContext;
import com.alibaba.chaosblade.exec.common.model.Model;
import com.alibaba.chaosblade.exec.spi.BusinessDataGetter;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author wufunc@gmail.com
 */
public class BusinessParamUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(BusinessParamUtil.class);
    private static GlobalContext businessParamContent = new GlobalContext(3600, 100000);

    private static Map<String, List<BusinessParam>> getAndParse(String target) throws Exception {
        Map<String, List<BusinessParam>> paramsMap = new HashMap<String, List<BusinessParam>>();
        List<StatusMetric> statusMetrics = ManagerFactory.getStatusManager().getExpByTarget(
                target);
        for (StatusMetric statusMetric : statusMetrics) {
            Model model = statusMetric.getModel();
            String flag = model.getMatcher().get(ModelConstant.BUSINESS_PARAMS);
            List<BusinessParam> businessParams = parseFromJsonStr(flag);
            paramsMap.put(ModelUtil.getIdentifier(model), businessParams);
        }
        return paramsMap;
    }

    public static List<BusinessParam> parseFromJsonStr(String str) {
        try {
            if (!StringUtil.isBlank(str)) {
                if (businessParamContent.containsKey(str)) {
                    return (List<BusinessParam>) businessParamContent.get(str);
                }
                List<BusinessParam> businessParams = Arrays.asList(JsonUtil.reader().readValue(str, BusinessParam[].class));
                businessParamContent.put(str, businessParams);
                return businessParams;
            }
        } catch (Exception exp) {
            LOGGER.warn("{} is not illegal json ", ModelConstant.BUSINESS_PARAMS);
        }
        return Collections.EMPTY_LIST;
    }

    public static Map<String, Map<String, String>> getAndParse(String target, BusinessDataGetter dataGetter) throws Exception {
        Map<String, List<BusinessParam>> businessParams = getAndParse(target);
        Map<String, Map<String, String>> result = new HashMap<String, Map<String, String>>();
        for (Map.Entry<String, List<BusinessParam>> entry : businessParams.entrySet()) {
            result.put(entry.getKey(), new HashMap<String, String>());
            for (BusinessParam businessParam : entry.getValue()) {
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

    private static String getValueFromSPI(BusinessParam businessParam, BusinessDataGetter dataGetter) {
        List<Object> objects = ManagerFactory.spiServiceManager().getServices(BusinessDataGetter.class.getName(), Thread.currentThread().getContextClassLoader());
        if (objects == null || objects.isEmpty()) {
            return null;
        }
        for (Object object : objects) {
            String tmpValue = null;
            try {
                tmpValue = ReflectUtil.invokeMethod(object, "get", new Object[]{businessParam.getKey()}, true);
            } catch (Exception e) {
                LOGGER.warn("get business value from spi class error,class :{}", object.getClass().getName(), e);
            }
            if (!StringUtils.isEmpty(tmpValue)) {
                return tmpValue;
            }
        }
        return null;
    }

    private static String getValueFromRpc(BusinessParam businessParam, BusinessDataGetter dataGetter) {
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
        spi("spi"), rpc("rpc");
        private String value;

        mode(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
