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

import com.alibaba.chaosblade.exec.common.util.ObjectsUtil;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/** @author RinaisSuper */
public abstract class AbstractScriptCache<K, V> implements ScriptCache<K, V> {

  private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
  protected Map<K, V> cacheMap;
  protected int cacheSize;

  public AbstractScriptCache(int cacheSize) {
    this.cacheSize = cacheSize;
  }

  @Override
  public void put(K k, V v) {
    ObjectsUtil.requireNonNull(v);
    Lock lock = readWriteLock.writeLock();
    lock.lock();
    try {
      cacheMap.put(k, v);
    } finally {
      lock.unlock();
    }
  }

  @Override
  public V get(K k) {
    Lock lock = readWriteLock.readLock();
    lock.lock();
    try {
      return cacheMap.get(k);
    } finally {
      lock.unlock();
    }
  }

  @Override
  public boolean evict(K k) {
    if (cacheMap.containsKey(k)) {
      Lock lock = readWriteLock.writeLock();
      lock.lock();
      try {
        cacheMap.remove(k);
        return true;
      } finally {
        lock.unlock();
      }
    }
    return false;
  }

  @Override
  public void clean() {
    Lock lock = readWriteLock.writeLock();
    lock.lock();
    try {
      cacheMap.clear();
    } finally {
      lock.unlock();
    }
  }
}
