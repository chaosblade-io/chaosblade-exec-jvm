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

package com.alibaba.chaosblade.exec.common.model.action.returnv;

import com.alibaba.chaosblade.exec.common.util.StringUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.reflect.Method;

/** @author Changjun Xiao */
public class DefaultReturnValueExecutor extends BaseReturnValueExecutor {

  public static final String NULL = "null";

  public DefaultReturnValueExecutor(ValueFlagSpec valueFlagSpec) {
    super(valueFlagSpec);
  }

  @Override
  public Object generateReturnValue(ClassLoader classLoader, Method method, String value) {
    if (StringUtil.isBlank(value) || value.equalsIgnoreCase(NULL)) {
      return null;
    }
    Class clazz = method.getReturnType();
    if (String.class == clazz) {
      return value;
    }
    if (byte.class == clazz || Byte.class == clazz) {
      return Byte.valueOf(value);
    }
    if (char.class == clazz || Character.class == clazz) {
      return value.charAt(0);
    }
    if (short.class == clazz || Short.class == clazz) {
      return Short.valueOf(value);
    }
    if (int.class == clazz || Integer.class == clazz) {
      return Integer.valueOf(value);
    }
    if (long.class == clazz || Long.class == clazz) {
      return Long.valueOf(value);
    }
    if (float.class == clazz || Float.class == clazz) {
      return Float.valueOf(value);
    }
    if (double.class == clazz || Double.class == clazz) {
      return Double.valueOf(value);
    }
    if (boolean.class == clazz || Boolean.class == clazz) {
      return Boolean.valueOf(value);
    }
    try {
      return new ObjectMapper().readValue(value, clazz);
    } catch (JsonProcessingException e) {
      throw new UnsupportedOperationException(
          "return value conversion failed. the return type is "
              + clazz.getName()
              + " the value is "
              + value);
    }
  }
}
