package com.alibaba.chaosblade.exec.common.model.action.threadpool;

/**
 * @Author qianfan
 * @package: com.alibaba.chaosblade.exec.common.model.action.threadpool
 * @Date 2020/12/22 11:44 上午
 */
public class WaitThread extends Thread {

    private final Object lock = new Object();

    public void unlock(){
        synchronized (lock) {
            lock.notify();
        }
    }

    @Override
    public void run() {
        synchronized (lock) {
            try {
                lock.wait();
            } catch (InterruptedException e) {
            }
        }
    }

}
