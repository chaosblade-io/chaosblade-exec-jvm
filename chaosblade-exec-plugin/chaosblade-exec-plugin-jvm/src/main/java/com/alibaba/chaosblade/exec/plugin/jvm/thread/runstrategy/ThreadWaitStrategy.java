package com.alibaba.chaosblade.exec.plugin.jvm.thread.runstrategy;

import com.alibaba.chaosblade.exec.common.model.action.threadpool.WaitThread;
import com.alibaba.chaosblade.exec.plugin.jvm.thread.ThreadRunStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author qianfan
 * @package: com.alibaba.chaosblade.exec.plugin.jvm.thread
 * @Date 2020/12/22 1:14 下午
 */
public class ThreadWaitStrategy implements ThreadRunStrategy {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadWaitStrategy.class);

    private static final Thread[][] threads = new Thread[1][];

    @Override
    public void start(int count) {
        if (null != threads[0]) {
            LOGGER.warn("threads index 0 is not null");
            return;
        }
        WaitThread[] waitThreads = new WaitThread[count];
        for (int i = 0; i < waitThreads.length; i++) {
            waitThreads[i] = new WaitThread();
            waitThreads[i].setName(String.format("ChaosBlade-Thread-%s", i));
            waitThreads[i].start();
        }
        threads[0] = waitThreads;
    }

    @Override
    public void stop() {
        WaitThread[] waitThreads = (WaitThread[]) threads[0];
        if (waitThreads == null || waitThreads.length < 1) {
            LOGGER.warn("threads index 0 is null");
            return;
        }

        for (int i = 0; i < waitThreads.length; i++) {
            waitThreads[i].unlock();
            waitThreads[i] = null;
        }
        threads[0] = null;
    }
}