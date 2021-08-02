package com.alibaba.chaosblade.exec.plugin.http.model;

import com.alibaba.chaosblade.exec.common.aop.CustomMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author shizhi.zhu@qunar.com
 */
public class CallPointMatcher implements CustomMatcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(CallPointMatcher.class);
    private static final CallPointMatcher INSTANCE = new CallPointMatcher();

    private CallPointMatcher() {}

    public static CallPointMatcher getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean match(String commandValue, Object originValue) {
        // match the class and method name who send the request.
        try {
            if (commandValue == null || commandValue.isEmpty()) {
                LOGGER.debug("commandValue is null, match success, commandValue:{}", commandValue);
                return true;
            }
            if (originValue == null) {
                LOGGER.debug("originValue is null, match fail, commandValue:{}", commandValue);
                return false;
            }
            StackTraceElement[] stackTrace = (StackTraceElement[])originValue;
            if (stackTrace.length == 0) {
                LOGGER.debug("stackTrace length is zero, match fail, commandValue:{}", commandValue);
                return false;
            }
            for (StackTraceElement element : stackTrace) {
                String callPoint = buildCallPoint(element);
                if (commandValue.equalsIgnoreCase(callPoint)) {
                    LOGGER.debug("call point equals, match success, commandValue:{}", commandValue);
                    return true;
                }
            }
        } catch (Exception e) {
            LOGGER.error("match call point fail, commandValue:{}, originValue:{}", commandValue, originValue, e);
        }
        return false;
    }

    private String buildCallPoint(StackTraceElement element) {
        return element.getClassName() + "|" + element.getMethodName();
    }

    @Override
    public boolean regexMatch(String commandValue, Object originValue) {
        return false;
    }
}
