package com.alibaba.xblade.exec.common.util;

/** @author Changjun Xiao */
public class ObjectsUtil {

  public static <T> T requireNonNull(T obj) {
    if (obj == null) {
      throw new NullPointerException();
    }
    return obj;
  }
}
