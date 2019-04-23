package com.alibaba.chaosblade.exec.plugin.jvm.oom.executor.impl;

import java.lang.reflect.Method;

import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.plugin.jvm.oom.JvmMemoryArea;
import com.alibaba.chaosblade.exec.plugin.jvm.oom.OomObject;
import com.alibaba.chaosblade.exec.plugin.jvm.oom.executor.JvmOomExecutor;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * @author haibin
 * @date 2019-04-18
 * @email haibin.lhb@alibaba-inc.com
 */
public class NoHeapJvmOomExecutor extends JvmOomExecutor {
    @Override
    public JvmMemoryArea supportArea() {
        return JvmMemoryArea.NOHEAP;
    }

    @Override
    public void stop(EnhancerModel enhancerModel) throws Exception {
        started.compareAndSet(true, false);
    }

    @Override
    protected void innerRun(EnhancerModel enhancerModel) {
        try {
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(OomObject.class);
            enhancer.setUseCache(false);
            enhancer.setCallback(new MethodInterceptor() {
                @Override
                public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy)
                    throws Throwable {
                    return proxy.invokeSuper(obj, args);
                }
            });
            enhancer.create();
        } catch (Throwable throwable) {
            handleThrowable(throwable);
        }
    }

    @Override
    protected void innerStop(EnhancerModel enhancerModel) {

    }

    @Override
    protected int threadCount() {
        return 5;
    }
}
