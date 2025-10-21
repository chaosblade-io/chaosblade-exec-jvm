/*
 * Copyright 2025 The ChaosBlade Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alibaba.chaosblade.exec.plugin.feign;

import com.alibaba.chaosblade.exec.common.aop.BeforeEnhancer;
import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherModel;
import com.alibaba.chaosblade.exec.common.util.ReflectUtil;
import com.alibaba.chaosblade.exec.common.util.StringUtils;
import java.lang.reflect.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** @author guoyu486@gmail.com */
public class FeignProducerEnhancer extends BeforeEnhancer implements FeignConstant {

  private static final Logger LOGGER = LoggerFactory.getLogger(FeignProducerEnhancer.class);

  @Override
  public EnhancerModel doBeforeAdvice(
      ClassLoader classLoader,
      String className,
      Object object,
      Method method,
      Object[] methodArguments)
      throws Exception {

    if (methodArguments == null || object == null) {
      LOGGER.warn("The necessary parameter is null.");
      return null;
    }
    String serviceName = getServiceName(object);
    String template = getTemplate(methodArguments);
    String url = getUrl(methodArguments);
    String templateUrl = StringUtils.isNotEmpty(template) ? template : url;
    LOGGER.info("feign serviceName:{},template:{},url:{}", serviceName, template, url);

    if (LOGGER.isDebugEnabled()) {
      LOGGER.info("feign :  serviceName:{},template:{}", serviceName, template);
    }
    MatcherModel matcherModel = new MatcherModel();
    matcherModel.add(SERVICE_NAME, serviceName);
    matcherModel.add(TEMPLATE_URL, templateUrl);
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("feign template url: {}", template);
    }
    return new EnhancerModel(classLoader, matcherModel);
  }

  private String getServiceName(Object curObject) throws Exception {
    // test in open-feign 3.1.1,2.1.1.RELEASE
    Object feignTargetObject = ReflectUtil.getFieldValue(curObject, "target", false);
    return ReflectUtil.invokeMethod(feignTargetObject, "name");
  }

  private String getTemplate(Object[] methodArguments) throws Exception {
    Object requestTemplateObj = methodArguments[0];
    Object urlBuilderObj = ReflectUtil.getFieldValue(requestTemplateObj, "url", false);
    if (urlBuilderObj == null) {
      // Compatible with RequestTemplate in feign-core version 10.2.0 and above
      Object uriTemplate = ReflectUtil.getFieldValue(requestTemplateObj, "uriTemplate", false);
      return uriTemplate != null ? uriTemplate.toString() : null;
    }
    return ReflectUtil.invokeMethod(urlBuilderObj, "toString");
  }

  private String getUrl(Object[] methodArguments) throws Exception {
    // spring-cloud-openfeign 3.0 ,feign-core 10.12
    Object requestTemplateObj = methodArguments[0];
    Object urlBuilderObj =
        ReflectUtil.getSuperclassFieldValue(requestTemplateObj, "uriTemplate", false);
    String url = ReflectUtil.getSuperclassFieldValue(urlBuilderObj, "template", false);
    return url;
  }
}
