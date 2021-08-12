package com.alibaba.chaosblade.exec.common.context;

/**
 * @author shizhi.zhu@qunar.com
 */
public class ThreadLocalContext {

    private static ThreadLocalContext DEFAULT = new ThreadLocalContext();
    private InheritableThreadLocal<Object> local = new InheritableThreadLocal<Object>();

    public static ThreadLocalContext getInstance() {
        return DEFAULT;
    }

    public void set(Object value) {
        local.set(value);
    }

    public Object get() {
        return local.get();
    }
}
