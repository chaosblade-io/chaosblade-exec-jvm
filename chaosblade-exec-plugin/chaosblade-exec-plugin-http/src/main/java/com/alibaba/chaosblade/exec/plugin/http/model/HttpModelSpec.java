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

package com.alibaba.chaosblade.exec.plugin.http.model;

import com.alibaba.chaosblade.exec.common.model.FrameworkModelSpec;
import com.alibaba.chaosblade.exec.common.model.action.ActionSpec;
import com.alibaba.chaosblade.exec.common.model.action.delay.DelayActionSpec;
import com.alibaba.chaosblade.exec.common.model.action.exception.ThrowCustomExceptionActionSpec;
import com.alibaba.chaosblade.exec.common.model.matcher.BusinessParamsMatcherSpec;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherSpec;
import com.alibaba.chaosblade.exec.plugin.http.HttpConstant;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author yuhan
 *
 * @package: com.alibaba.chaosblade.exec.plugin.http.model @Date 2019-05-21 10:10
 */
public class HttpModelSpec extends FrameworkModelSpec {

  public HttpModelSpec() {
    addActionExample();
  }

  private void addActionExample() {
    List<ActionSpec> actions = getActions();
    for (ActionSpec action : actions) {
      if (action instanceof DelayActionSpec) {
        action.setLongDesc("HTTP client delay experiment");
        action.setExample(
            "# Do a delay 3s experiment with HTTP request URI = https://www.taobao.com for HttpClient4\n"
                + "blade create http delay --httpclient4 --uri https://www.taobao.com --time 3000\n\n"
                + "# Do a delay 3s experiment with HTTP request URI = https://www.taobao.com for HttpClient3\n"
                + "blade create http delay --httpclient3 --uri https://www.taobao.com --time 3000\n\n"
                + "# Do a delay 3s experiment with HTTP request URI = https://www.taobao.com for RestTemplate\n"
                + "blade create http delay --rest --uri https://www.taobao.com --time 3000\n\n"
                + "#Do a delay 3s experiment with HTTP request URI = https://www.taobao.com for OkHttp3\n"
                + "blade create http delay --okhttp3 --uri https://www.taobao.com --time 3000");
      }
      if (action instanceof ThrowCustomExceptionActionSpec) {
        action.setLongDesc("HTTP client throws custom exception experiment");
        action.setExample(
            "# Do a throws custom exception with HTTP request URI = https://www.taobao.com/ for HttpClient4\n"
                + "blade c http throwCustomException --httpclient4 --exception=java.lang.Exception --exception-message=customException --uri=https://www.taobao.com/");
      }
    }
  }

  @Override
  public String getShortDesc() {
    return "http experiment";
  }

  @Override
  public String getLongDesc() {
    return "http experiment for testing service delay and exception.";
  }

  @Override
  protected List<MatcherSpec> createNewMatcherSpecs() {
    ArrayList<MatcherSpec> matcherSpecs = new ArrayList<MatcherSpec>();
    matcherSpecs.add(new RestTemplateMatcherSpec());
    matcherSpecs.add(new HttpClient4MatcherSpec());
    matcherSpecs.add(new HttpClient3MatcherSpec());
    matcherSpecs.add(new Okhttp3MatcherSpec());
    matcherSpecs.add(new AsyncHttpClientMatcherSpec());
    matcherSpecs.add(new UriMatcherDefSpec());
    matcherSpecs.add(new CallPointMatcherSpec());
    matcherSpecs.add(new BusinessParamsMatcherSpec());
    return matcherSpecs;
  }

  @Override
  public String getTarget() {
    return HttpConstant.TARGET_NAME;
  }
}
