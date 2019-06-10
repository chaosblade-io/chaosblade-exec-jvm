/*
 * Copyright 1999-2019 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alibaba.chaosblade.exec.plugin.jvm;

/**
 * @author haibin
 * @date 2019-04-18
 */
public interface JvmConstant {

    String FLAG_NAME_MEMORY_AREA = "area";

    /**
     * enabled System.gc()
     */
    String FLAG_NAME_ENABLE_SYSTEM_GC = "enableSystemGc";

    String FLAG_NAME_THREAD_COUNT = "threads";

    Integer FLAG_VALUE_OOM_THREAD_COUNT = 1;

    String ACTION_OUT_OF_MEMORY_ERROR_NAME = "outofmemoryerror";

    String ACTION_OUT_OF_MEMORY_ERROR_ALIAS = "oom";

    String ACTION_CPU_FULL_LOAD_NAME = "cpufullload";

    String ACTION_CPU_FULL_LOAD_ALIAS = "cfl";

    String FLAG_NAME_CPU_COUNT = "cpu-count";

    String ACTION_DYNAMIC_SCRIPT_NAME = "script";

    String FLAG_NAME_SCRIPT_TYPE = "script-type";

    String FLAG_NAME_SCRIPT_FILE = "script-file";

    String FLAG_NAME_SCRIPT_CONTENT = "script-content";

    String FLAG_NAME_SCRIPT_CONTENT_DECODE = "script-content-decode";

    String FLAG_NAME_SCRIPT_NAME = "script-name";

    String SCRIPT_TYPE_JAVA = "java";

    String SCRIPT_TYPE_GROOVY = "groovy";

    String GROOVY_VAL_KEY = "params";
}
