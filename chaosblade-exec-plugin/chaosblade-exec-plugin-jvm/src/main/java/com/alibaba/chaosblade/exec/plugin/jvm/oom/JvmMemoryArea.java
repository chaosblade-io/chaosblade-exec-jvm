package com.alibaba.chaosblade.exec.plugin.jvm.oom;

/**
 * @author RinaisSuper
 * @date 2019-04-18
 * @email rinalhb@icloud.com
 */
public enum JvmMemoryArea {

  /**
   * heap area,mainly contains Young Gen(Eden,S0,S1),Old Gen the jvm options:-Xmx -Xmx,also can
   * specify Young Gen size by -Xmn)
   */
  HEAP,

  /** no heap area the jvm option: -XX:MaxPermSize below jdk8 or --XX:MetaspaceSize in jdk8 */
  NOHEAP,

  /**
   * off heap,the memory area outside heap,mainly means direct buff
   *
   * @see {@link ByteBuffer}
   */
  OFFHEAP;

  public static String[] getAreaNames() {
    String[] names = new String[JvmMemoryArea.values().length];
    for (int i = 0; i < JvmMemoryArea.values().length; i++) {
      names[i] = JvmMemoryArea.values()[i].name();
    }
    return names;
  }

  public static JvmMemoryArea nameOf(String name) {
    for (JvmMemoryArea jvmMemoryArea : JvmMemoryArea.values()) {
      if (jvmMemoryArea.name().equalsIgnoreCase(name)) {
        return jvmMemoryArea;
      }
    }
    return null;
  }
}
