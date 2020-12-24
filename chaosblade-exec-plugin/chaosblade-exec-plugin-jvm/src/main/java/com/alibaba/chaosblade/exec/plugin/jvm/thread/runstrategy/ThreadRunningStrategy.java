package com.alibaba.chaosblade.exec.plugin.jvm.thread.runstrategy;

import com.alibaba.chaosblade.exec.plugin.jvm.thread.ThreadRunStrategy;

/**
 * @Author qianfan
 * @package: com.alibaba.chaosblade.exec.plugin.jvm.thread
 * @Date 2020/12/22 1:19 下午
 */
public class ThreadRunningStrategy implements ThreadRunStrategy {

    private static volatile boolean running = false;

    private Thread[] threads = null;

    @Override
    public void start(int count) {
        running = true;
        threads = new Thread[count];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (running){}
                }
            });
            threads[i].setName(String.format("ChaosBlade-Thread-%s", i));
            threads[i].start();
        }
    }

    @Override
    public void stop() {
        running = false;
        for (Thread thread : threads) {
            thread = null;
        }
        threads = null;
    }
}
