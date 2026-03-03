package com.alibaba.xblade.exec.common.model.action.threadpool;

import com.alibaba.xblade.exec.common.aop.PredicateResult;
import com.alibaba.xblade.exec.common.constant.CategoryConstants;
import com.alibaba.xblade.exec.common.model.FlagSpec;
import com.alibaba.xblade.exec.common.model.action.ActionExecutor;
import com.alibaba.xblade.exec.common.model.action.ActionModel;
import com.alibaba.xblade.exec.common.model.action.BaseActionSpec;
import com.alibaba.xblade.exec.common.util.StringUtils;
import java.util.ArrayList;
import java.util.List;

/** @author Changjun Xiao */
public class ThreadPoolFullActionSpec extends BaseActionSpec {

  public static final String NAME = "threadpoolfull";

  public ThreadPoolFullActionSpec() {
    super(new DefaultThreadPoolFullExecutor());
  }

  public ThreadPoolFullActionSpec(ActionExecutor actionExecutor) {
    super(actionExecutor);
  }

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public String[] getAliases() {
    return new String[] {"tpf"};
  }

  @Override
  public String getShortDesc() {
    return "Thread pool full";
  }

  @Override
  public String getLongDesc() {
    if (StringUtils.isNotBlank(super.getLongDesc())) {
      return super.getLongDesc();
    }
    return "Thread pool full";
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
