package com.alibaba.chaosblade.exec.plugin.http;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

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
    public static final String OKHTTP3 = "okhttp3";
    public static final String ASYNC_HTTP_TARGET_NAME = "asyncHttpClient";
    public static final String getURI = "getURI";
    public static final String CALL_POINT_KEY = "call-point";

    public static final Map<String, Method> methodMap = new HashMap<String, Method>();

    public static final int DEFAULT_TIMEOUT = 60000;

    public static final String REQUEST_ID = "c_request_id";

    public static final String REST_TARGET_NAME = "rest";
    public static final String HTTPCLIENT3_TARGET_NAME = "httpclient3";
    public static final String HTTPCLIENT4_TARGET_NAME = "httpclient4";
    public static final String OKHTTP3_TARGET_NAME = "okhttp3";

}
