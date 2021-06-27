package com.alibaba.chaosblade.exec.plugin.log.logback;

import com.alibaba.chaosblade.exec.common.aop.PointCut;
import com.alibaba.chaosblade.exec.common.aop.matcher.clazz.*;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.MethodMatcher;
import com.alibaba.chaosblade.exec.common.aop.matcher.method.NameMethodMatcher;

/**
 * @author shizhi.zhu@qunar.com
 */
public class LogbackPointCut implements PointCut {
    private static final String LOGBACK_CONSOLE_STREAM_SYS_OUT = "ch.qos.logback.core.joran.spi.ConsoleTarget$1";
    private static final String LOGBACK_CONSOLE_STREAM_SYS_ERROR = "ch.qos.logback.core.joran.spi.ConsoleTarget$2";
    private static final String JAVA_OUTPUT_STREAM = "java.io.OutputStream";
    private static final String LOGBACK_FILE_OUTPUT_STREAM = "ch.qos.logback.core.recovery.ResilientOutputStreamBase";
    private static final String LOGBACK_METHOD_NAME = "flush";

    @Override
    public ClassMatcher getClassMatcher() {
        ClassMatcher consoleTargetClassNameMatcher = new OrClassMatcher()
                .or(new NameClassMatcher(LOGBACK_CONSOLE_STREAM_SYS_OUT))
                .or(new NameClassMatcher(LOGBACK_CONSOLE_STREAM_SYS_ERROR));
        ClassMatcher consoleMatcher = new AndClassMatcher()
                .and(new SuperClassMatcher(JAVA_OUTPUT_STREAM))
                .and(consoleTargetClassNameMatcher);
        ClassMatcher logbackAllClassMatcher = new OrClassMatcher()
                .or(new NameClassMatcher(LOGBACK_FILE_OUTPUT_STREAM))
                .or(consoleMatcher);
        return logbackAllClassMatcher;
    }

    @Override
    public MethodMatcher getMethodMatcher() {
        return new NameMethodMatcher(LOGBACK_METHOD_NAME);
    }
}
