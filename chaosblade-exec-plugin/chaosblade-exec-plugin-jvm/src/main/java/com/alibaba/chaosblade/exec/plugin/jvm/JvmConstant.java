/*
 * Copyright 2025 The ChaosBlade Authors
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
 * @author RinaisSuper
 * @date 2019-04-18
 */
public interface JvmConstant {

  String FLAG_NAME_MEMORY_AREA = "area";

  int ONE_MB = 1024 * 1024;

  String FLAG_NAME_OOM_HAPPEN_MODE = "wild-mode";

  String FLAG_NAME_OOM_BLOCK = "block";

  String FLAG_OOM_ERROR_INTERVAL = "interval";

  Integer MIN_OOM_HAPPEN_INTERVAL_IN_MILLS = 500;

  String ACTION_OUT_OF_MEMORY_ERROR_NAME = "OutOfMemoryError";

  String ACTION_CPU_FULL_LOAD_NAME = "cpufullload";

  String ACTION_CPU_FULL_LOAD_ALIAS = "cfl";

  String ACTION_CODE_CACHE_FILLING_NAME = "CodeCacheFilling";

  String ACTION_CODE_CACHE_FILLING_ALIAS = "ccf";

  String FLAG_NAME_CPU_COUNT = "cpu-count";

  String ACTION_DYNAMIC_SCRIPT_NAME = "script";

  String FLAG_NAME_SCRIPT_TYPE = "script-type";

  String FLAG_NAME_SCRIPT_FILE = "script-file";

  String FLAG_NAME_SCRIPT_CONTENT = "script-content";

  String FLAG_NAME_SCRIPT_CONTENT_DECODE = "script-content-decode";

  String FLAG_NAME_SCRIPT_NAME = "script-name";

  String FLAG_NAME_EXTERNAL_JAR = "external-jar";

  String FLAG_NAME_EXTERNAL_JAR_PATH = "external-jar-path";

  String SCRIPT_TYPE_JAVA = "java";

  String SCRIPT_TYPE_GROOVY = "groovy";

  String GROOVY_VAL_KEY = "params";

  String SCRIPT_INVOKE_RETURN = "return";

  String SCRIPT_INVOKE_TARGET = "target";

  String JAR_FILE_SUFFIX = ".jar";

  String FILE_PROTOCOL = "file:";

  String THREAD_FULL = "threadfull";
  String ACTION_THREAD_FULL_ALIAS = "tfl";
  String ACTION_THREAD_COUNT = "thread-count";

  String ACTION_THREAD_WAIT = "wait";
  String ACTION_THREAD_RUNNING = "running";

  String FLAG_FULL_GC_INTERVAL = "interval";
  String FLAG_FULL_GC_TOTAL_COUNT = "effect-count";
  String ACTION_FULL_GC_NAME = "full-gc";
  String ACTION_FULL_GC_ALIAS = "fgc";
}
