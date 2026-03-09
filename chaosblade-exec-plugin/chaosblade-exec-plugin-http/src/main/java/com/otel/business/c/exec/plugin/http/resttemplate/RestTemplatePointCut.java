package com.otel.business.c.exec.plugin.http.resttemplate;

import com.otel.business.c.exec.common.aop.PointCut;
import com.otel.business.c.exec.common.aop.matcher.clazz.ClassMatcher;
import com.otel.business.c.exec.common.aop.matcher.clazz.NameClassMatcher;
import com.otel.business.c.exec.common.aop.matcher.method.MethodMatcher;
import com.otel.business.c.exec.common.aop.matcher.method.NameMethodMatcher;

/**
 * @Author yuhan
 *
 * @package: com.otel.business.c.exec.plugin.http.resttemplate @Date 2019-05-22 16:21
 */
public class RestTemplatePointCut implements PointCut {

  @Override
  public ClassMatcher getClassMatcher() {
    return new NameClassMatcher("org.springframework.web.client.RestTemplate");
  }

  @Override
  public MethodMatcher getMethodMatcher() {
    return new NameMethodMatcher("doExecute");
  }
}
