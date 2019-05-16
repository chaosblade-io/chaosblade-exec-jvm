package com.alibaba.chaosblade.exec.common.model.action.threadpool;

/**
 * @author Changjun Xiao
 */
public class InterruptableRunnable implements Runnable {

    public static final long MAX_SLEEP_TIME_IN_MILLS = 24 * 60 * 60 * 60 * 1000L;

    @Override
    public void run() {
        try {
            Thread.sleep(MAX_SLEEP_TIME_IN_MILLS);
        } catch (InterruptedException e) {
        }
    }
}
