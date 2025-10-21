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

package com.alibaba.chaosblade.exec.common.exception;

/** @author Changjun Xiao */
public class InterruptProcessException extends Exception {

  private final State state;
  private final Object response;

  private InterruptProcessException(State state, Object response) {
    this.state = state;
    this.response = response;
  }

  public static InterruptProcessException throwReturnImmediately(final Object object)
      throws InterruptProcessException {
    throw new InterruptProcessException(State.RETURN_IMMEDIATELY, object);
  }

  public static InterruptProcessException throwThrowsImmediately(final Throwable throwable)
      throws InterruptProcessException {
    throw new InterruptProcessException(State.THROWS_IMMEDIATELY, throwable);
  }

  public State getState() {
    return state;
  }

  public Object getResponse() {
    return response;
  }

  /** Process flow state */
  public enum State {

    /** return */
    RETURN_IMMEDIATELY,

    /** throw exception */
    THROWS_IMMEDIATELY,
  }
}
