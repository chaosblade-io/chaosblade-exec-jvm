package com.alibaba.xblade.exec.common.center;

import java.util.List;

public interface SPIServiceManager extends ManagerService {
  List<Object> getServices(String className, ClassLoader classLoader);
}
