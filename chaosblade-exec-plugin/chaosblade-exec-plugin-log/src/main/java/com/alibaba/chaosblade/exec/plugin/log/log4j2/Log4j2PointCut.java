package com.alibaba.chaosblade.exec.plugin.log.log4j2;

import com.alibaba.chaosblade.exec.common.aop.PointCut;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.*;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.MethodMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.NameMethodMatcher;

/**
 * @author orion233
 */
public class Log4j2PointCut implements PointCut {

    private static final String LOG4J_CONSOLE = "org.apache.logging.log4j.core.appender.rolling.RollingRandomAccessFileManager";
    private static final String LOG4J_METHOD_NAME = "writeToDestination";

    @Override
    public ClassMatcher getClassMatcher() {
        ClassMatcher log4j2AllClassMatcher = new NameClassMatcher(LOG4J_CONSOLE);
        return log4j2AllClassMatcher;
    }

    @Override
    public MethodMatcher getMethodMatcher() {
        return new NameMethodMatcher(LOG4J_METHOD_NAME);
    }
}
