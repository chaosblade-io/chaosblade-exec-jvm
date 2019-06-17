package com.alibaba.chaosblade.exec.plugin.http;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author yuhan
 * @package: com.alibaba.chaosblade.exec.plugin.http
 * @Date 2019-05-22 16:28
 */
public class HttpConstant {

    public static final String URI_KEY = "uri";
    public static final String REST_KEY = "rest";
    public static final String HTTPCLIENT4 = "httpclient4";
    public static final String HTTPCLIENT3 = "httpclient3";
    public static final String getURI = "getURI";

    public static final Map<String, Method> methodMap = new HashMap<String, Method>();
}
