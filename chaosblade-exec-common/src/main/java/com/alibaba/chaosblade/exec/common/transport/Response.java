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

package com.alibaba.chaosblade.exec.common.transport;

import com.alibaba.chaosblade.exec.common.util.JsonUtil;

/** @author Changjun Xiao */
public class Response {

  private String requestId;
  private int code;
  private boolean success;
  private String result;
  private String error;

  public Response() {}

  public Response(String requestId, int code, boolean success, String result, String error) {
    this.requestId = requestId;
    this.code = code;
    this.success = success;
    this.result = result;
    this.error = error;
  }

  private Response(Code code, boolean success, String error, String result) {
    this.code = code.getCode();
    this.success = success;
    this.result = result;
    this.error = error;
  }

  /**
   * Construct a successful response with given object.
   *
   * @param result result object
   * @param <T> type of the result
   * @return constructed server response
   */
  public static <T> Response ofSuccess(String result) {
    return new Response(Code.OK, true, null, result);
  }

  /**
   * Construct a failed response with given error message.
   *
   * @param code
   * @param error
   * @return constructed server response
   */
  public static <T> Response ofFailure(Code code, String error) {
    return new Response(code, false, error, null);
  }

  public String getRequestId() {
    return requestId;
  }

  public void setRequestId(String requestId) {
    this.requestId = requestId;
  }

  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }

  public String getResult() {
    return result;
  }

  public void setResult(String result) {
    this.result = result;
  }

  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Response)) {
      return false;
    }

    Response response = (Response) o;

    if (code != response.code) {
      return false;
    }
    if (success != response.success) {
      return false;
    }
    if (requestId != null ? !requestId.equals(response.requestId) : response.requestId != null) {
      return false;
    }
    if (result != null ? !result.equals(response.result) : response.result != null) {
      return false;
    }
    return error != null ? error.equals(response.error) : response.error == null;
  }

  @Override
  public int hashCode() {
    int result1 = requestId != null ? requestId.hashCode() : 0;
    result1 = 31 * result1 + code;
    result1 = 31 * result1 + (success ? 1 : 0);
    result1 = 31 * result1 + (result != null ? result.hashCode() : 0);
    result1 = 31 * result1 + (error != null ? error.hashCode() : 0);
    return result1;
  }

  @Override
  public String toString() {
    try {
      return JsonUtil.writer().writeValueAsString(this);
    } catch (Throwable e) {
    }
    return "Response{"
        + "requestId='"
        + requestId
        + '\''
        + ", code="
        + code
        + ", success="
        + success
        + ", result='"
        + result
        + '\''
        + ", error='"
        + error
        + '\''
        + '}';
  }

  /** Response code */
  public enum Code {
    OK(200, "success"),
    NOT_FOUND(404, "request handler not found"),
    ILLEGAL_PARAMETER(405, "illegal parameter"),
    DUPLICATE_INJECTION(406, "duplicate injection"),
    SERVER_ERROR(500, "server error"),
    ILLEGAL_STATE(504, "illegal state");
    private int code;
    private String msg;

    Code(int code, String msg) {
      this.code = code;
      this.msg = msg;
    }

    public int getCode() {
      return code;
    }

    public String getMsg() {
      return msg;
    }
  }
}
