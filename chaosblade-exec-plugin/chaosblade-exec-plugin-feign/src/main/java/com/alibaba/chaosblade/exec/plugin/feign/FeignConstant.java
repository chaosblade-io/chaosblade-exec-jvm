package com.alibaba.chaosblade.exec.plugin.feign;

/** @author guoyu486@gmail.com */
public interface FeignConstant {

  String TARGET_NAME = "feign";

  String ENHANCER_CLASS = "feign.SynchronousMethodHandler";

  String METHOD = "executeAndDecode";

  String SERVICE_NAME = "service-name";

  String TEMPLATE_URL = "url";
}
