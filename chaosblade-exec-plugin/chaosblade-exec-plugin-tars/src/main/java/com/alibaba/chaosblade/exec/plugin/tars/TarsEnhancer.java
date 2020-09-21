package com.alibaba.chaosblade.exec.plugin.tars;

import com.alibaba.chaosblade.exec.common.aop.BeforeEnhancer;
import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.common.model.action.delay.TimeoutExecutor;

import java.lang.reflect.Method;

/**
 * @author saikei
 * @email lishiji@huya.com
 */
public abstract class TarsEnhancer extends BeforeEnhancer {

    /**
     * doBeforeAdvice
     * @param classLoader
     * @param className
     * @param object
     * @param method
     * @param methodArguments
     * @return
     * @throws Exception
     */
    @Override
    public abstract EnhancerModel doBeforeAdvice(ClassLoader classLoader, String className, Object object,
                                        Method method, Object[] methodArguments) throws Exception;


    /**
     * Create timeout executor
     *
     * @param classLoader
     * @param timeout
     * @param className
     * @return
     */
    protected abstract TimeoutExecutor createTimeoutExecutor(ClassLoader classLoader, long timeout,
                                                             String className);

}
