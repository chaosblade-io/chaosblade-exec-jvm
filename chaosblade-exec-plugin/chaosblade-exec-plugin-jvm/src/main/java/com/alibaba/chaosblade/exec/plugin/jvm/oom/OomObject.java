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

package com.alibaba.chaosblade.exec.plugin.jvm.oom;

import java.util.ArrayList;
import java.util.List;

/**
 * oom object
 *
 * @author RinaisSuper
 * @date 2019-04-18
 * @email rinalhb@icloud.com
 */
public class OomObject {

  List<byte[]> list;

  public OomObject() {
    this(1);
  }

  /** size,unit mb */
  public OomObject(int size) {
    if (size <= 0) {
      size = 1;
    }
    list = new ArrayList<byte[]>();
    for (int i = 0; i < size; i++) {
      list.add(createObject());
    }
  }

  /**
   * create 1mb object
   *
   * @return
   */
  private byte[] createObject() {
    return new byte[1024 * 1024];
  }
}
