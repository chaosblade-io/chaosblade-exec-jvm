package com.alibaba.chaosblade.exec.plugin.jvm.oom;

import java.util.ArrayList;
import java.util.List;

/**
 * oom object
 *
 * @author RinaisSuper
 * @date 2019-04-18
 * @email rinalhb@icloud.com
 */
public class OomObject {

   List<byte[]> list;

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
        list = new ArrayList<byte[]>();
        for (int i = 0; i < size; i++) {
            list.add(createObject());
        }
    }

    /**
     * create 1mb object
     *
     * @return
     */
    private byte[] createObject() {
        return new byte[1024 * 1024];
    }
}
