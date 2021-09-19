package com.alibaba.chaosblade.exec.common.context;

import java.util.Map;

/**
 * @author shizhi.zhu@qunar.com
 */
public class ThreadLocalContext {

    private static ThreadLocalContext DEFAULT = new ThreadLocalContext();
    private InheritableThreadLocal<Content> local = new InheritableThreadLocal<Content>();

    public static ThreadLocalContext getInstance() {
        return DEFAULT;
    }

    public void set(Content value) {
        local.set(value);
    }

    public Content get() {
        return local.get();
    }

    public static class Content{
        private  StackTraceElement[] stackTraceElements;
        private Map<String, Map<String, String>> businessData;

        public StackTraceElement[] getStackTraceElements() {
            return stackTraceElements;
        }

        public void setStackTraceElements(StackTraceElement[] stackTraceElements) {
            this.stackTraceElements = stackTraceElements;
        }

        public Map<String, Map<String, String>> getBusinessData() {
            return businessData;
        }

        public void settValue(Map<String, Map<String, String>>  businessData) {
            this.businessData = businessData;
        }
    }
}
