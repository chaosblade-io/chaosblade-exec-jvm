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

package com.alibaba.chaosblade.exec.common.aop;

import com.alibaba.chaosblade.exec.common.model.Model;
import com.alibaba.chaosblade.exec.common.model.action.ActionModel;
import com.alibaba.chaosblade.exec.common.model.action.delay.TimeoutExecutor;
import com.alibaba.chaosblade.exec.common.model.action.threadpool.ThreadPoolFullExecutor;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherModel;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/** @author Changjun Xiao */
public class EnhancerModel {

  private String target;
  private ActionModel actionModel;
  private MatcherModel matcherModel;
  private ClassLoader classLoader;
  private Object object;
  private Method method;
  private Object[] methodArguments;
  private Object returnValue;
  private Map<String, CustomMatcher> customMatcher;

  private TimeoutExecutor timeoutExecutor;
  private ThreadPoolFullExecutor threadPoolFullExecutor;

  public EnhancerModel(ClassLoader classLoader, MatcherModel matcherModel) {
    this.classLoader = classLoader;
    this.matcherModel = matcherModel;
    this.customMatcher = new HashMap<String, CustomMatcher>();
  }

  public String getTarget() {
    return target;
  }

  public EnhancerModel setTarget(String target) {
    this.target = target;
    return this;
  }

  public String getAction() {
    return actionModel.getName();
  }

  public MatcherModel getMatcherModel() {
    return matcherModel;
  }

  public void addMatcher(String key, String value) {
    this.matcherModel.add(key, value);
  }

  public String getActionFlag(String key) {
    return actionModel.getFlag(key);
  }

  public void addActionFlag(String key, String value) {
    actionModel.addFlag(key, value);
  }

  public ClassLoader getClassLoader() {
    return classLoader;
  }

  public EnhancerModel setClassLoader(ClassLoader classLoader) {
    this.classLoader = classLoader;
    return this;
  }

  public Object getObject() {
    return object;
  }

  public EnhancerModel setObject(Object object) {
    this.object = object;
    return this;
  }

  public Method getMethod() {
    return method;
  }

  public EnhancerModel setMethod(Method method) {
    this.method = method;
    return this;
  }

  public Object[] getMethodArguments() {
    return methodArguments;
  }

  public EnhancerModel setMethodArguments(Object[] methodArguments) {
    this.methodArguments = methodArguments;
    return this;
  }

  public Object getReturnValue() {
    return returnValue;
  }

  public EnhancerModel setReturnValue(Object returnValue) {
    this.returnValue = returnValue;
    return this;
  }

  public TimeoutExecutor getTimeoutExecutor() {
    return timeoutExecutor;
  }

  public EnhancerModel setTimeoutExecutor(TimeoutExecutor timeoutExecutor) {
    this.timeoutExecutor = timeoutExecutor;
    return this;
  }

  public ThreadPoolFullExecutor getThreadPoolFullExecutor() {
    return threadPoolFullExecutor;
  }

  public void setThreadPoolFullExecutor(ThreadPoolFullExecutor threadPoolFullExecutor) {
    this.threadPoolFullExecutor = threadPoolFullExecutor;
  }

  public void merge(Model model) {
    this.actionModel = model.getAction();
  }

  public void addCustomMatcher(String key, Object value, CustomMatcher matcher) {
    this.matcherModel.add(key, value);
    this.customMatcher.put(key, matcher);
  }

  public CustomMatcher getMatcher(String key) {
    return this.customMatcher.get(key);
  }
}
