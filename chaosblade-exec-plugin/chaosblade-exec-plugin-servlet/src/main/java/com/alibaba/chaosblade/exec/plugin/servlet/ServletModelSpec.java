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

package com.alibaba.chaosblade.exec.plugin.servlet;

import com.alibaba.chaosblade.exec.common.model.FrameworkModelSpec;
import com.alibaba.chaosblade.exec.common.model.action.ActionSpec;
import com.alibaba.chaosblade.exec.common.model.action.delay.DelayActionSpec;
import com.alibaba.chaosblade.exec.common.model.action.exception.ThrowCustomExceptionActionSpec;
import com.alibaba.chaosblade.exec.common.model.matcher.BusinessParamsMatcherSpec;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherSpec;
import com.alibaba.chaosblade.exec.plugin.servlet.code.ModifyHttpCodeActionSpec;
import java.util.ArrayList;
import java.util.List;

/** @author Changjun Xiao */
public class ServletModelSpec extends FrameworkModelSpec {

  public ServletModelSpec() {
    addModifyHttpAction();
    addActionExample();
  }

  private void addModifyHttpAction() {
    ModifyHttpCodeActionSpec modifyHttpCodeActionSpec = new ModifyHttpCodeActionSpec();
    modifyHttpCodeActionSpec.addMatcherDesc(new ServletQueryStringMatcherSpec());
    modifyHttpCodeActionSpec.addMatcherDesc(new ServletQueryStringRegexPatternMatcherSpec());
    modifyHttpCodeActionSpec.addMatcherDesc(new ServletMethodMatcherSpec());
    modifyHttpCodeActionSpec.addMatcherDesc(new ServletRequestPathMatcherSpec());
    modifyHttpCodeActionSpec.addMatcherDesc(new ServletRequestPathRegexPatternMatcherSpec());
    modifyHttpCodeActionSpec.setLongDesc("Servlet return custom status code(4xx,5xx)");
    modifyHttpCodeActionSpec.setExample(
        "# Request to http://localhost:8080/dubbodemo/servlet/path?name=bob return 404\n"
            + "blade c servlet mc --requestpath /dubbodemo/servlet/path --code=404\n\n");
    addActionSpec(modifyHttpCodeActionSpec);
  }

  private void addActionExample() {
    List<ActionSpec> actions = getActions();
    for (ActionSpec action : actions) {
      if (action instanceof DelayActionSpec) {
        action.setLongDesc("Servlet delay experiment, support servlet springMVC webX");
        action.setExample(
            "# Request to http://localhost:8080/dubbodemo/servlet/path?name=bob delays 3s, effect two requests\n"
                + "blade c servlet delay --time 3000 --requestpath /dubbodemo/servlet/path --effect-count 2\n\n"
                + "# The request parameter is name=family, the delay is 2 seconds, the delay time floats up and down for 1 second, the impact range is 50% of the request, and the debug log is turned on to troubleshoot the problem\n"
                + "blade c servlet delay --time 2000 --offset 1000 --querystring name=family --effect-percent 50 --debug");
      } else if (action instanceof ThrowCustomExceptionActionSpec) {
        action.setLongDesc(
            "Servlet throw custom exception experiment, support servlet springMVC webX");
        action.setExample(
            "# Request to http://localhost:8080/dubbodemo/hello throws custom exception, effect three requests\n"
                + "blade c servlet throwCustomException --exception org.springframework.beans.BeansException --exception-message mock-beans-exception --requestpath /hello --effect-count 3");
      }
    }
  }

  @Override
  public String getTarget() {
    return ServletConstant.TARGET_NAME;
  }

  @Override
  public String getShortDesc() {
    return "java servlet experiment";
  }

  @Override
  public String getLongDesc() {
    return "Java servlet experiment, support path, query string, request method matcher";
  }

  @Override
  protected List<MatcherSpec> createNewMatcherSpecs() {
    ArrayList<MatcherSpec> matcherSpecs = new ArrayList<MatcherSpec>();
    matcherSpecs.add(new ServletQueryStringMatcherSpec());
    matcherSpecs.add(new ServletQueryStringRegexPatternMatcherSpec());
    matcherSpecs.add(new ServletMethodMatcherSpec());
    matcherSpecs.add(new ServletRequestPathMatcherSpec());
    matcherSpecs.add(new ServletRequestPathRegexPatternMatcherSpec());
    matcherSpecs.add(new BusinessParamsMatcherSpec());
    return matcherSpecs;
  }
}
