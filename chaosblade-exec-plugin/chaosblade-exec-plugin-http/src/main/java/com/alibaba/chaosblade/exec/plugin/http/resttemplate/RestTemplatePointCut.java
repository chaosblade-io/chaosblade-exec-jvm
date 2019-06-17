package com.alibaba.chaosblade.exec.plugin.http.resttemplate;

import com.alibaba.chaosblade.exec.common.aop.PointCut;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.ClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.NameClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.MethodMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.NameMethodMatcher;

/**
 * @Author yuhan
 * @package: com.alibaba.chaosblade.exec.plugin.http.resttemplate
 * @Date 2019-05-22 16:21
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
