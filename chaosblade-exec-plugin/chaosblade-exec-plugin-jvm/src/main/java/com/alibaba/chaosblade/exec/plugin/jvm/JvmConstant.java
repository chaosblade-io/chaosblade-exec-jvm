package com.alibaba.chaosblade.exec.plugin.jvm;

/**
 * @author haibin
 * @date 2019-04-18
 */
public final class JvmConstant {

    public static String FLAG_NAME_MEMORY_AREA = "area";

    /**
     * enabled System.gc()
     */
    public static String FLAG_NAME_ENABLE_SYSTEM_GC = "enableSystemGc";

    public static String FLAG_NAME_THREAD_COUNT = "threads";

    public static Integer FLAG_VALUE_OOM_THREAD_COUNT = 1;

    public static String ACTION_OUT_OF_MEMORY_ERROR_NAME = "outofmemoryerror";

    public static String ACTION_OUT_OF_MEMORY_ERROR_ALIAS = "oom";

    public static String ACTION_CPU_FULL_LOAD_NAME = "cpufullload";

    public static String ACION_CPU_FULL_LOAD_ALIAS = "cfl";

    public static String FLAG_NAME_CPU_COUNT = "cpu-count";
}
