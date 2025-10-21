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

package com.alibaba.chaosblade.exec.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/** @author Changjun Xiao */
public class ReflectUtil {

  /**
   * @param obj
   * @param methodName
   * @param <T>
   * @return
   * @throws Exception
   */
  public static <T> T invokeMethod(Object obj, String methodName) throws Exception {
    return invokeMethod(obj, methodName, new Object[] {});
  }

  /**
   * @param obj
   * @param methodName
   * @param args
   * @param <T>
   * @return
   * @throws Exception
   */
  public static <T> T invokeMethod(Object obj, String methodName, Object[] args) throws Exception {
    return invokeMethod(obj, methodName, args, false);
  }

  /**
   * Invoke method by reflect
   *
   * @param obj
   * @param methodName
   * @param args
   * @param throwException
   * @param <T>
   * @return
   * @throws Exception
   */
  public static <T> T invokeMethod(
      Object obj, String methodName, Object[] args, boolean throwException) throws Exception {
    if (obj == null) {
      return null;
    }
    return invoke(obj.getClass(), obj, methodName, args, throwException);
  }

  /**
   * Invoke static method
   *
   * @param clazz
   * @param methodName
   * @param args
   * @param throwException
   * @param <T>
   * @return
   */
  public static <T> T invokeStaticMethod(
      Class<?> clazz, String methodName, Object[] args, boolean throwException) throws Exception {
    return invoke(clazz, null, methodName, args, throwException);
  }

  private static <T> T invoke(
      Class<?> clazz, Object obj, String methodName, Object[] args, boolean throwException)
      throws Exception {
    try {
      Method method = getMethod(clazz, methodName, args);
      method.setAccessible(true);
      return (T) method.invoke(obj, args);
    } catch (Exception e) {
      if (throwException) {
        throw e;
      }
    }
    return null;
  }

  /**
   * Get method object TODO
   *
   * @param clazz
   * @param methodName
   * @param args
   * @return
   * @throws NoSuchMethodException
   */
  public static Method getMethod(Class<?> clazz, String methodName, Object[] args)
      throws NoSuchMethodException {
    Class[] argsClass = new Class[args.length];
    for (int i = 0, j = args.length; i < j; i++) {
      if (args[i] instanceof Boolean) {
        argsClass[i] = boolean.class;
        continue;
      }
      if (args[i] instanceof Integer) {
        argsClass[i] = int.class;
        continue;
      }
      if (args[i] instanceof Long) {
        argsClass[i] = long.class;
        continue;
      }
      if (args[i] == null) {
        continue;
      }
      argsClass[i] = args[i].getClass();
    }

    return getMethod(clazz, methodName, argsClass);
  }

  /**
   * Get method by name and descriptor
   *
   * @param clazz
   * @param methodDescriptor
   * @return
   * @throws NoSuchMethodException
   */
  public static Method getMethod(Class<?> clazz, String methodDescriptor, String methodName)
      throws NoSuchMethodException {
    Method[] declaredMethods = clazz.getDeclaredMethods();
    for (Method method : declaredMethods) {
      String desc = desc(method);
      if (method.getName().equals(methodName) && desc.equals(methodDescriptor)) {
        return method;
      }
    }

    if (clazz.getSuperclass() != null) {
      return getMethod(clazz.getSuperclass(), methodDescriptor, methodName);
    }

    throw new NoSuchMethodException(methodDescriptor + " descriptor");
  }

  public static String desc(Class<?> returnType) {
    if (returnType.isPrimitive()) {
      return getPrimitiveLetter(returnType);
    } else {
      return returnType.isArray()
          ? "[" + desc(returnType.getComponentType())
          : "L" + type(returnType) + ";";
    }
  }

  public static String desc(Method method) {
    Class<?>[] types = method.getParameterTypes();
    StringBuilder buf = new StringBuilder(types.length + 1 << 4);
    buf.append('(');

    for (int i = 0; i < types.length; ++i) {
      buf.append(desc(types[i]));
    }

    buf.append(')');
    buf.append(desc(method.getReturnType()));
    return buf.toString();
  }

  public static Method getMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes)
      throws NoSuchMethodException {
    Method method;
    try {
      method = clazz.getMethod(methodName, parameterTypes);
    } catch (Exception e) {
      try {
        method = clazz.getDeclaredMethod(methodName, parameterTypes);
      } catch (NoSuchMethodException e1) {
        return getMethodByName(clazz, methodName, parameterTypes);
      }
    }
    return method;
  }

  private static Method getMethodByName(
      Class<?> clazz, String methodName, Class<?>... parameterTypes) throws NoSuchMethodException {
    if (clazz == Object.class) {
      throw new NoSuchMethodException();
    }
    Method[] methods = clazz.getDeclaredMethods();
    for (Method method : methods) {
      if (!method.getName().equals(methodName)) {
        continue;
      }
      Class<?>[] methodParamTypes = method.getParameterTypes();
      if (methodParamTypes == null && parameterTypes == null) {
        return method;
      }
      if (methodParamTypes == null || parameterTypes == null) {
        continue;
      }
      if (methodParamTypes.length != parameterTypes.length) {
        continue;
      }
      boolean match = true;
      for (int i = 0; i < parameterTypes.length; i++) {
        if (parameterTypes[i] == null) {
          continue;
        }
        if (!methodParamTypes[i].isAssignableFrom(parameterTypes[i])) {
          match = false;
          break;
        }
      }
      if (match) {
        return method;
      }
    }
    return getMethodByName(clazz.getSuperclass(), methodName, parameterTypes);
  }

  /**
   * Get object fields
   *
   * @param obj
   * @param fieldName
   * @param throwException
   * @param <T>
   * @return
   * @throws Exception
   */
  public static <T> T getFieldValue(Object obj, String fieldName, boolean throwException)
      throws Exception {
    Field field;
    try {
      try {
        if (obj == null) {
          return null;
        }
        field = obj.getClass().getField(fieldName);
        field.setAccessible(true);
        return (T) field.get(obj);
      } catch (Exception e) {
        field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return (T) field.get(obj);
      }
    } catch (Exception e) {
      if (throwException) {
        throw e;
      }
    }
    return null;
  }

  /**
   * Get object fields
   *
   * @param obj
   * @param fieldName
   * @param throwException
   * @param <T>
   * @return
   * @throws Exception
   */
  public static <T> T getSuperclassFieldValue(Object obj, String fieldName, boolean throwException)
      throws Exception {
    try {
      if (obj == null) {
        return null;
      }
      for (Class clazz = obj.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
        try {
          Field field = clazz.getDeclaredField(fieldName);
          field.setAccessible(true);
          return (T) field.get(obj);
        } catch (NoSuchFieldException e) {
        }
      }
    } catch (Exception e) {
      if (throwException) {
        throw e;
      }
    }
    return null;
  }

  public static boolean isAssignableFrom(
      ClassLoader classLoader, Class<?> clazz, String clazzName) {
    if (clazz == null || clazzName == null) {
      return false;
    }
    if (clazz.getName().equals(clazzName) || clazz.getSimpleName().equals(clazzName)) {
      return true;
    }
    if (classLoader == null) {
      return false;
    }
    try {
      Class<?> parentClazz = classLoader.loadClass(clazzName);
      return parentClazz.isAssignableFrom(clazz);
    } catch (ClassNotFoundException e) {
      return false;
    }
  }

  public static String type(Class<?> parameterType) {
    if (parameterType.isArray()) {
      return "[" + desc(parameterType.getComponentType());
    } else if (!parameterType.isPrimitive()) {
      String clsName = parameterType.getName();
      return clsName.replace('.', '/');
    } else {
      return getPrimitiveLetter(parameterType);
    }
  }

  public static String getPrimitiveLetter(Class<?> type) {
    if (Integer.TYPE == type) {
      return "I";
    } else if (Void.TYPE == type) {
      return "V";
    } else if (Boolean.TYPE == type) {
      return "Z";
    } else if (Character.TYPE == type) {
      return "C";
    } else if (Byte.TYPE == type) {
      return "B";
    } else if (Short.TYPE == type) {
      return "S";
    } else if (Float.TYPE == type) {
      return "F";
    } else if (Long.TYPE == type) {
      return "J";
    } else if (Double.TYPE == type) {
      return "D";
    } else {
      throw new IllegalStateException(
          "Type: " + type.getCanonicalName() + " is not a primitive type");
    }
  }
}
