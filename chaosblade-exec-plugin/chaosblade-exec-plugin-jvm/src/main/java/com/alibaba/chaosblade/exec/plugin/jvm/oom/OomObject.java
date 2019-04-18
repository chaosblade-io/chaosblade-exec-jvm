package com.alibaba.chaosblade.exec.plugin.jvm.oom;

/**
 * oom object
 *
 * @author haibin
 * @date 2019-04-18
 * @email haibin.lhb@alibaba-inc.com
 */
public class OomObject {

    public OomObject() {
        this(1);
    }

    private StringBuilder stringBuilder;

    /**
     * size,unit mb
     */
    public OomObject(int size) {
        stringBuilder = new StringBuilder();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                stringBuilder.append(createObject());
            }
        }
    }

    /**
     * create 2mb object
     *
     * @return
     */
    private String createObject() {
        byte[] bytes = new byte[1024 * 512];
        return new String(bytes);
    }
}
