package com.otel.business.c.exec.plugin.servlet.code;

import com.otel.business.c.exec.common.aop.PredicateResult;
import com.otel.business.c.exec.common.constant.CategoryConstants;
import com.otel.business.c.exec.common.model.FlagSpec;
import com.otel.business.c.exec.common.model.action.ActionModel;
import com.otel.business.c.exec.common.model.action.BaseActionSpec;
import com.otel.business.c.exec.common.util.StringUtil;
import java.util.ArrayList;
import java.util.List;

/** @author yefei */
public class ModifyHttpCodeActionSpec extends BaseActionSpec {

  private static final HttpCodeFlagSpec HTTP_CODE_FLAG_SPEC = new HttpCodeFlagSpec();

  public ModifyHttpCodeActionSpec() {
    super(new ModifyHttpCodeActionExecutor(HTTP_CODE_FLAG_SPEC));
  }

  @Override
  public String getName() {
    return "modifyCode";
  }

  @Override
  public String[] getAliases() {
    return new String[] {"mc"};
  }

  @Override
  public String getShortDesc() {
    return "update action spec";
  }

  @Override
  public String getLongDesc() {
    return "update action spec";
  }

  @Override
  public List<FlagSpec> getActionFlags() {
    List<FlagSpec> flagSpecs = new ArrayList<FlagSpec>();
    flagSpecs.add(HTTP_CODE_FLAG_SPEC);
    return flagSpecs;
  }

  @Override
  public PredicateResult predicate(ActionModel actionModel) {
    if (StringUtil.isBlank(actionModel.getFlag(HTTP_CODE_FLAG_SPEC.getName()))) {
      return PredicateResult.fail("less http code argument");
    }
    return PredicateResult.success();
  }

  @Override
  public String[] getCategories() {
    return new String[] {CategoryConstants.JAVA_DATA_TAMPER};
  }
}
