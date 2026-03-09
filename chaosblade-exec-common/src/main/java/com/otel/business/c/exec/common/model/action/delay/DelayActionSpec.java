/*
 * Copyright 1999-2019 Alibaba Group Holding Ltd.
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

package com.otel.business.c.exec.common.model.action.delay;

import com.otel.business.c.exec.common.aop.PredicateResult;
import com.otel.business.c.exec.common.constant.CategoryConstants;
import com.otel.business.c.exec.common.model.FlagSpec;
import com.otel.business.c.exec.common.model.action.ActionModel;
import com.otel.business.c.exec.common.model.action.BaseActionSpec;
import com.otel.business.c.exec.common.util.StringUtil;
import com.otel.business.c.exec.common.util.StringUtils;
import java.util.Arrays;
import java.util.List;

/** @author Changjun Xiao */
public class DelayActionSpec extends BaseActionSpec {

  private static TimeFlagSpec timeFlag = new TimeFlagSpec();
  private static TimeOffsetFlagSpec offsetFlag = new TimeOffsetFlagSpec();

  public DelayActionSpec() {
    super(new DefaultDelayExecutor(timeFlag, offsetFlag));
  }

  @Override
  public String getName() {
    return "delay";
  }

  @Override
  public String[] getAliases() {
    return new String[0];
  }

  @Override
  public String getShortDesc() {
    return "delay time";
  }

  @Override
  public String getLongDesc() {
    if (StringUtils.isNotBlank(super.getLongDesc())) {
      return super.getLongDesc();
    }
    return "delay time...";
  }

  @Override
  public List<FlagSpec> getActionFlags() {
    return Arrays.asList(timeFlag, offsetFlag);
  }

  @Override
  public PredicateResult predicate(ActionModel actionModel) {
    if (StringUtil.isBlank(actionModel.getFlag(timeFlag.getName()))) {
      return PredicateResult.fail("less time argument");
    }
    return PredicateResult.success();
  }

  @Override
  public String[] getCategories() {
    return new String[] {CategoryConstants.JAVA_DELAY};
  }
}
