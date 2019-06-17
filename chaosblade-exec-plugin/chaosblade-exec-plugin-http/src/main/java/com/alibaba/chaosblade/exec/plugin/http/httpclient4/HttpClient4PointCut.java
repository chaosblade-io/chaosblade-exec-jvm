package com.alibaba.chaosblade.exec.plugin.http.httpclient4;

import com.alibaba.chaosblade.exec.common.aop.PointCut;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.ClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.NameClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.OrClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.*;

/**
 * @Author yuhan
 * @package: com.alibaba.chaosblade.exec.plugin.http.httpclient4
 * @Date 2019-05-22 16:10
 */
public class HttpClient4PointCut implements PointCut {

    @Override
    public ClassMatcher getClassMatcher() {
        OrClassMatcher classMatcher = new OrClassMatcher();
        classMatcher
                .or(new NameClassMatcher("org.apache.http.impl.client.AbstractHttpClient"))
                .or(new NameClassMatcher("org.apache.http.impl.client.MinimalHttpClient"))
                .or(new NameClassMatcher("org.apache.http.impl.client.InternalHttpClient"));
        return classMatcher;
    }

    @Override
    public MethodMatcher getMethodMatcher() {
        AndMethodMatcher methodMatcher = new AndMethodMatcher();
        OrMethodMatcher orMethodMatcher = new OrMethodMatcher();
        orMethodMatcher.or(new NameMethodMatcher("execute")).or(new NameMethodMatcher("doExecute"));

        ParameterMethodMatcher parameterMethodMatcher = new ParameterMethodMatcher(new String[] {
                "org.apache.http.HttpHost",
                "org.apache.http.HttpRequest",
                "org.apache.http.protocol.HttpContext"}, 3,
                ParameterMethodMatcher.EQUAL);
        methodMatcher.
                and(orMethodMatcher).
                and(parameterMethodMatcher);
        return methodMatcher;
    }
}
