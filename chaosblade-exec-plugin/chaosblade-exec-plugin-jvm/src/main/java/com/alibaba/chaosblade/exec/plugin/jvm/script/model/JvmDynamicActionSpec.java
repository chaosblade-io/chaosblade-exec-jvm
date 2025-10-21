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

package com.alibaba.chaosblade.exec.plugin.jvm.script.model;

import com.alibaba.chaosblade.exec.common.aop.PredicateResult;
import com.alibaba.chaosblade.exec.common.constant.CategoryConstants;
import com.alibaba.chaosblade.exec.common.model.FlagSpec;
import com.alibaba.chaosblade.exec.common.model.action.ActionModel;
import com.alibaba.chaosblade.exec.common.model.action.BaseActionSpec;
import com.alibaba.chaosblade.exec.common.plugin.ClassNameMatcherSpec;
import com.alibaba.chaosblade.exec.common.plugin.MethodAfterMatcherSpec;
import com.alibaba.chaosblade.exec.common.plugin.MethodNameMatcherSpec;
import com.alibaba.chaosblade.exec.plugin.jvm.JvmConstant;
import java.util.ArrayList;
import java.util.List;

/** @author Changjun Xiao */
public class JvmDynamicActionSpec extends BaseActionSpec {

  public JvmDynamicActionSpec() {
    super(new DynamicScriptExecutor());
    addMatcherDesc(new ClassNameMatcherSpec());
    addMatcherDesc(new MethodNameMatcherSpec(true));
    addMatcherDesc(new MethodAfterMatcherSpec());
  }

  @Override
  public String getName() {
    return JvmConstant.ACTION_DYNAMIC_SCRIPT_NAME;
  }

  @Override
  public String[] getAliases() {
    return new String[0];
  }

  @Override
  public String getShortDesc() {
    return "Dynamically execute custom scripts";
  }

  @Override
  public String getLongDesc() {
    return "Dynamically execute custom scripts";
  }

  @Override
  public List<FlagSpec> getActionFlags() {
    ArrayList<FlagSpec> flagSpecs = new ArrayList<FlagSpec>();
    flagSpecs.add(new ScriptFileFlagSpec());
    flagSpecs.add(new ScriptTypeFlagSpec());
    flagSpecs.add(new ScriptContentFlagSpec());
    flagSpecs.add(new ScriptNameFlagSpec());
    flagSpecs.add(new ScriptExternalJarFlagSpec());
    flagSpecs.add(new ScriptExternalJarPathFlagSpec());
    return flagSpecs;
  }

  @Override
  public PredicateResult predicate(ActionModel actionModel) {
    return PredicateResult.success();
  }

  @Override
  public String getExample() {
    return "# Using script-Content to specify walk-through script content, without adding a script-type parameter, it defaults to a Java script and calls the Java engine parser.\n"
        + "blade c jvm script --classname com.example.controller.DubboController --methodname call --script-content aW1wb3J0IGphdmEudXRpbC5NYXA7CgppbXBvcnQgY29tLmV4YW1wbGUuY29udHJvbGxlci5DdXN0b21FeGNlcHRpb247CgovKioKICogQGF1dGhvciBDaGFuZ2p1biBYaWFvCiAqLwpwdWJsaWMgY2xhc3MgRXhjZXB0aW9uU2NyaXB0IHsKICAgIHB1YmxpYyBPYmplY3QgcnVuKE1hcDxTdHJpbmcsIE9iamVjdD4gcGFyYW1zKSB0aHJvd3MgQ3VzdG9tRXhjZXB0aW9uIHsKICAgICAgICBwYXJhbXMucHV0KCIxIiwgMTExTCk7CiAgICAgICAgLy9yZXR1cm4gIk1vY2sgVmFsdWUiOwogICAgICAgIC8vdGhyb3cgbmV3IEN1c3RvbUV4Y2VwdGlvbigiaGVsbG8iKTsKICAgICAgICByZXR1cm4gbnVsbDsKICAgIH0KfQo=  --script-name exception\n\n"
        + "# Use the script-file parameter to specify the file experiment\n"
        + "blade c jvm script --classname com.example.controller.DubboController --methodname call --script-file /tmp/ExceptionScript.java --script-name exception\n\n"
        + "# The groovy script experiment scenario is executed with the same parameters as above, but the --script-type Groovy parameter must be added\n"
        + "blade c jvm script --classname com.example.controller.DubboController --methodname call --script-file /tmp/GroovyScript.groovy --script-name exception --script-type groovy\n\n"
        + "# Load external jar file\n"
        + "blade c jvm script --classname com.example.controller.DubboController --methodname call --external-jar file:/temp/chaosblade-extends.jar --script-content xxx";
  }

  @Override
  public String[] getCategories() {
    return new String[] {CategoryConstants.JAVA_CUSTOM};
  }
}
