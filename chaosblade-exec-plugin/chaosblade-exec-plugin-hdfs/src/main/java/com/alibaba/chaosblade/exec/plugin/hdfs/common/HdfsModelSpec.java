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

package com.alibaba.chaosblade.exec.plugin.hdfs.common;

import com.alibaba.chaosblade.exec.common.model.FrameworkModelSpec;
import com.alibaba.chaosblade.exec.common.model.action.ActionSpec;
import com.alibaba.chaosblade.exec.common.model.action.delay.DelayActionSpec;
import com.alibaba.chaosblade.exec.common.model.action.exception.ThrowCustomExceptionActionSpec;
import com.alibaba.chaosblade.exec.common.model.matcher.MatcherSpec;
import com.alibaba.chaosblade.exec.plugin.hdfs.common.flag.HdfsFileMatcherSpec;
import com.alibaba.chaosblade.exec.plugin.hdfs.restful.flag.HdfsRestfulOperationMatcherSpec;
import java.util.ArrayList;
import java.util.List;

public class HdfsModelSpec extends FrameworkModelSpec {
  public HdfsModelSpec() {
    List<ActionSpec> actionSpecList = getActions();
    for (ActionSpec actionSpec : actionSpecList) {
      if (actionSpec instanceof DelayActionSpec) {
        actionSpec.setLongDesc("Conduct delay experiment on HDFS file operations.");
        actionSpec.setExample(
            "# Perform a delay experiment for HDFS client.\n"
                + "EXAMPLE: blade create hdfs delay --time 1000 [--file example/demo.tmp] [--op open|getfilestatus|create|...]");
      }
      if (actionSpec instanceof ThrowCustomExceptionActionSpec) {
        actionSpec.setLongDesc(
            "Conduct throwing custom exception experiment on HDFS file operations.");
        actionSpec.setExample(
            "# Perform a throwing custom exception experiment for HDFS client.\n"
                + "EXAMPLE: blade create hdfs throwCustomException --exception java.lang.Exception [--file example/demo.tmp] [--op open|getfilestatus|create|...]");
      }
    }
  }

  @Override
  protected List<MatcherSpec> createNewMatcherSpecs() {
    ArrayList<MatcherSpec> matcherSpecList = new ArrayList<>();
    matcherSpecList.add(new HdfsFileMatcherSpec());
    matcherSpecList.add(new HdfsRestfulOperationMatcherSpec());
    return matcherSpecList;
  }

  @Override
  public String getTarget() {
    return HdfsConstant.TARGET_NAME;
  }

  @Override
  public String getShortDesc() {
    return "HDFS Chaos Experiment Plugin.";
  }

  @Override
  public String getLongDesc() {
    return "A ChaosBlade plugin to conduct chaos experiments on HDFS, which provides several kinds of chaos injection, "
        + "such as making delay of response, throwing custom exception, blocking file operations etc.";
  }
}
