package com.otel.business.c.exec.plugin.jvm.thread;

public interface ThreadRunStrategy {

  void start(int count);

  void stop();
}
