package com.alibaba.chaosblade.exec.plugin.security;

/**
 * @author liubin@njzfit.cn
 */
public class SecurityConstant {

    public static final String PLUGIN_NAME = "security";

    public static final String PARAM_USERNAME = "username";

    public static final String CLASS_UserDetailsService = "org.springframework.security.core.userdetails.UserDetailsService";
    public static final String METHOD_UserDetailsService$loadUserByUsername = "loadUserByUsername";
}
