package com.alibaba.chaosblade.exec.plugin.jvm.thread;

public interface ThreadRunStrategy {

    void start(int count);

    void stop();
}