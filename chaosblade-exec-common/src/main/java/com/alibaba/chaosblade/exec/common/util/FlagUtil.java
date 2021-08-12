package com.alibaba.chaosblade.exec.common.util;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.chaosblade.exec.common.center.ManagerFactory;
import com.alibaba.chaosblade.exec.common.center.StatusMetric;

/**
 * @author shizhi.zhu@qunar.com
 */
public class FlagUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(FlagUtil.class);

    public static boolean hasFlag(String target, String flag) {
        try {
            List<StatusMetric> metrics = ManagerFactory.getStatusManager().getExpByTarget(target);
            for (StatusMetric metric : metrics) {
                Map<String, Object> matchers = metric.getModel().getMatcher().getMatchers();
                if (matchers.containsKey(flag)) {
                    return true;
                }
            }
        } catch (Exception e) {
            LOGGER.error("Get flag value from metrics fail, target:{}, flag:{}", target, flag, e);
        }
        return false;
    }
}
