package com.alibaba.chaosblade.exec.plugin.feign;

import java.lang.reflect.Method;

import com.alibaba.chaosblade.exec.common.aop.BeforeEnhancer;
import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherModel;
import com.alibaba.chaosblade.exec.common.util.ReflectUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author guoyu486@gmail.com
 */
public class FeignProducerEnhancer extends BeforeEnhancer implements FeignConstant {

    private static final Logger LOGGER = LoggerFactory.getLogger(FeignProducerEnhancer.class);

    @Override
    public EnhancerModel doBeforeAdvice(ClassLoader classLoader, String className, Object object, Method method,
        Object[] methodArguments) throws Exception {

        if (methodArguments == null || object == null) {
            LOGGER.warn("The necessary parameter is null.");
            return null;
        }
        String serviceName = getServiceName(methodArguments);
        String template = getTemplate(methodArguments);
        MatcherModel matcherModel = new MatcherModel();
        matcherModel.add(SERVICE_NAME, serviceName);
        matcherModel.add(TEMPLATE_URL, template);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("feign template url: {}", template);
        }
        return new EnhancerModel(classLoader, matcherModel);
    }

    private String getServiceName(Object[] methodArguments) throws Exception {
        Object requestTemplateObj = methodArguments[0];
        Object feignTargetObject = ReflectUtil.getFieldValue(requestTemplateObj, "feignTarget", false);
        return ReflectUtil.invokeMethod(feignTargetObject, "name");
    }

    private String getTemplate(Object[] methodArguments) throws Exception {
        Object requestTemplateObj = methodArguments[0];
        Object uriTemplateObj = ReflectUtil.getFieldValue(requestTemplateObj, "uriTemplate", false);
        return ReflectUtil.getSuperclassFieldValue(uriTemplateObj, "template", false);
    }
}
