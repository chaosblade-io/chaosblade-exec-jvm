package com.alibaba.chaosblade.exec.common.center;

import java.util.List;

public interface SPIServiceManager extends ManagerService{
    List<Object> getServices(String className, ClassLoader classLoader);
}
