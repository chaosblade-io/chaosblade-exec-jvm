package com.alibaba.chaosblade.exec.plugin.security;

/**
 * @author liubin477@163.com
 */
public class SecurityConstant {

    public static final String PLUGIN_NAME = "security";

    public static final String PARAM_USERNAME = "username";

    public static final String CLASS_AuthenticationManager = "org.springframework.security.authentication.AuthenticationManager";
    public static final String CLASS_Authentication = "org.springframework.security.core.Authentication";
    public static final String METHOD_Authentication$getName = "getName";
    public static final String METHOD_AuthenticationManager$authenticate = "authenticate";
}
