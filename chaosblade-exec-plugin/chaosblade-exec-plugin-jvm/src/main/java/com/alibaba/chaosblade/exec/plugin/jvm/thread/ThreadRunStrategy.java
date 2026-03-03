package com.alibaba.xblade.exec.plugin.jvm.thread;

public interface ThreadRunStrategy {

  void start(int count);

  void stop();
}
