package com.alibaba.chaosblade.exec.plugin.jvm.oom;

/**
 * oom object
 *
 * @author RinaisSuper
 * @date 2019-04-18
 * @email rinalhb@icloud.com
 */
public class OomObject {

    String[] strings;

    public OomObject() {
        this(1);
    }

    /**
     * size,unit mb
     */
    public OomObject(int size) {
        if (size <= 0) {
            size = 1;
        }
        strings = new String[size];
        for (int i = 0; i < size; i++) {
            strings[i] = new String(createObject());
        }
    }

    /**
     * create 1mb object
     *
     * @return
     */
    private byte[] createObject() {
        return new byte[1024 * 300];
    }
}
