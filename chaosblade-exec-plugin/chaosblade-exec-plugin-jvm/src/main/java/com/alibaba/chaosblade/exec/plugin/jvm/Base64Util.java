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

import org.apache.commons.codec.binary.Base64;

/** @author Changjun Xiao */
public class Base64Util {

  private static final String CR_LF_UNIX = "\n";
  private static final String CR_LF_WIN = "\r\n";

  /**
   * Base64 encode
   *
   * @param bytes
   * @param isChunked split string to lines length is 76
   * @return
   */
  public static String encode(byte[] bytes, boolean isChunked) {
    byte[] byte64 = Base64.encodeBase64(bytes, isChunked);
    String result = new String(byte64);
    String endChars;
    if (result.endsWith((endChars = CR_LF_UNIX)) || result.endsWith((endChars = CR_LF_WIN))) {
      result = result.substring(0, result.length() - endChars.length());
    }
    return result;
  }

  /**
   * Base64 decode
   *
   * @param bytes
   * @return
   */
  public static String decode(byte[] bytes) {
    byte[] decodeBase64 = Base64.decodeBase64(bytes);
    String result = new String(decodeBase64);
    String endChars;
    if (result.endsWith((endChars = CR_LF_UNIX)) || result.endsWith((endChars = CR_LF_WIN))) {
      result = result.substring(0, result.length() - endChars.length());
    }
    return result;
  }
}
