package com.alibaba.chaosblade.exec.plugin.http.enhancer;

import com.alibaba.chaosblade.exec.common.aop.Enhancer;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/** @author shizhi.zhu@quanr.com */
public class InternalEnhancerContainer {
  private Map<String, Enhancer> enhancerMap = new ConcurrentHashMap<String, Enhancer>();

  public void add(Enhancer enhancer) {
    InternalPointCut annotation = enhancer.getClass().getAnnotation(InternalPointCut.class);
    if (annotation != null) {
      enhancerMap.put(join(annotation.className(), annotation.methodName()), enhancer);
    }
  }

  public Enhancer get(String className, String methodName) {
    return enhancerMap.get(join(className, methodName));
  }

  private String join(String className, String methodName) {
    return className + "#" + methodName;
  }
}
