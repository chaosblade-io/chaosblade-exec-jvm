package com.alibaba.xblade.exec.common.model.action.connpool;

import com.alibaba.xblade.exec.common.aop.PredicateResult;
import com.alibaba.xblade.exec.common.constant.CategoryConstants;
import com.alibaba.xblade.exec.common.model.FlagSpec;
import com.alibaba.xblade.exec.common.model.action.ActionExecutor;
import com.alibaba.xblade.exec.common.model.action.ActionModel;
import com.alibaba.xblade.exec.common.model.action.BaseActionSpec;
import java.util.ArrayList;
import java.util.List;

/** @author Changjun Xiao */
public class ConnectionPoolFullActionSpec extends BaseActionSpec {
  public static final String NAME = "connectionpoolfull";

  public ConnectionPoolFullActionSpec(ActionExecutor actionExecutor) {
    super(actionExecutor);
  }

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public String[] getAliases() {
    return new String[] {"cpf"};
  }

  @Override
  public String getShortDesc() {
    return "Connection pool full";
  }

  @Override
  public String getLongDesc() {
    return "Connection pool full";
  }

  @Override
  public List<FlagSpec> getActionFlags() {
    return new ArrayList<FlagSpec>();
  }

  @Override
  public PredicateResult predicate(ActionModel actionModel) {
    return PredicateResult.success();
  }

  @Override
  public String[] getCategories() {
    return new String[] {CategoryConstants.JAVA_RESOURCE_CPU};
  }
}
