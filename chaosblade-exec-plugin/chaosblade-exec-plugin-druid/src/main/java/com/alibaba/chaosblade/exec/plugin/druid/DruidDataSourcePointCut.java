package com.alibaba.chaosblade.exec.plugin.druid;

import com.alibaba.chaosblade.exec.common.aop.PointCut;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.ClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.NameClassMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.AndMethodMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.MethodMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.NameMethodMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.ParameterMethodMatcher;

/**
 * @author Changjun Xiao
 */
public class DruidDataSourcePointCut implements PointCut {

    @Override
    public ClassMatcher getClassMatcher() {
        return new NameClassMatcher("com.alibaba.druid.pool.DruidDataSource");
    }

    @Override
    public MethodMatcher getMethodMatcher() {
        // public DruidPooledConnection getConnection(long maxWaitMillis) throws SQLException
        AndMethodMatcher methodMatcher = new AndMethodMatcher();
        methodMatcher.and(new NameMethodMatcher("getConnection")).and(
            new ParameterMethodMatcher(1, ParameterMethodMatcher.EQUAL));
        return methodMatcher;
    }
}
