package com.alibaba.chaosblade.exec.plugin.http.httpclient3;

import com.alibaba.chaosblade.exec.common.aop.PointCut;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.ClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.NameClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.AndMethodMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.MethodMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.NameMethodMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.ParameterMethodMatcher;

/**
 * @Author yuhan
 * @package: com.alibaba.chaosblade.exec.plugin.http.httpclient4
 * @Date 2019-05-22 16:10
 */
public class HttpClient3PointCut implements PointCut {

    @Override
    public ClassMatcher getClassMatcher() {
        return new NameClassMatcher("org.apache.commons.httpclient.HttpClient");
    }

    @Override
    public MethodMatcher getMethodMatcher() {
        AndMethodMatcher methodMatcher = new AndMethodMatcher();
        ParameterMethodMatcher parameterMethodMatcher = new ParameterMethodMatcher(new String[]{
                "org.apache.commons.httpclient.HostConfiguration",
                "org.apache.commons.httpclient.HttpMethod",
                "org.apache.commons.httpclient.HttpState"}, 3,
                ParameterMethodMatcher.EQUAL);
        methodMatcher.
                and(new NameMethodMatcher("executeMethod")).
                and(parameterMethodMatcher);
        return methodMatcher;
    }
}
