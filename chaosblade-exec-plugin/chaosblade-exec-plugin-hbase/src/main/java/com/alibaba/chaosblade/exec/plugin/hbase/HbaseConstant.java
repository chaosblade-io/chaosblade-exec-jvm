package com.alibaba.chaosblade.exec.plugin.hbase;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author yuhan
 * @package: com.alibaba.chaosblade.exec.plugin.http
 * @Date 2019-05-22 16:28
 */
public class HbaseConstant {

    public static final String TARGET_NAME = "hbase";
    public static final String TABLE = "table";

    public static final Map<String, Method> methodMap = new HashMap<String, Method>();
}
