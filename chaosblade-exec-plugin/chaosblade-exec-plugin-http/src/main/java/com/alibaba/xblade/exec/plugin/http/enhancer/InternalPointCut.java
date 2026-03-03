package com.alibaba.xblade.exec.plugin.http.enhancer;

import java.lang.annotation.*;

/** @author shizhi.zhu@qunar.com */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface InternalPointCut {
  String className();

  String methodName();
}
