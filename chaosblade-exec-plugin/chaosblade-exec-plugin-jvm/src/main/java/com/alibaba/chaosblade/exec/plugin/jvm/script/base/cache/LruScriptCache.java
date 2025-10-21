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

package com.alibaba.chaosblade.exec.plugin.jvm.script.base.cache;

import java.util.LinkedHashMap;
import java.util.Map;

/** @author RinaisSuper */
public class LruScriptCache<Key, Value> extends AbstractScriptCache<Key, Value> {

  public LruScriptCache(int size) {
    super(size);
    setSize(size);
  }

  private void setSize(int size) {
    cacheMap =
        new LinkedHashMap<Key, Value>(size + 1, 1.0f, true) {
          @Override
          protected boolean removeEldestEntry(final Map.Entry eldest) {
            return LruScriptCache.this.removeEldestEntry(size());
          }
        };
  }

  /** Removes the eldest entry if current cache size exceed cache size. */
  protected boolean removeEldestEntry(final int currentSize) {
    if (cacheSize == 0) {
      return false;
    }
    return currentSize > cacheSize;
  }
}
