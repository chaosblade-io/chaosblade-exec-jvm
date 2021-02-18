package com.alibaba.chaosblade.exec.plugin.jvm.cpu;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.common.aop.PredicateResult;
import com.alibaba.chaosblade.exec.common.constant.CategoryConstants;
import com.alibaba.chaosblade.exec.common.model.FlagSpec;
import com.alibaba.chaosblade.exec.common.model.Model;
import com.alibaba.chaosblade.exec.common.model.action.ActionModel;
import com.alibaba.chaosblade.exec.common.model.action.BaseActionSpec;
import com.alibaba.chaosblade.exec.common.model.action.DirectlyInjectionAction;
import com.alibaba.chaosblade.exec.plugin.jvm.JvmConstant;
import com.alibaba.chaosblade.exec.plugin.jvm.StoppableActionExecutor;

/**
 * @author Changjun Xiao
 */
public class JvmCpuFullLoadActionSpec extends BaseActionSpec implements DirectlyInjectionAction {

    public JvmCpuFullLoadActionSpec() {
        super(new JvmCpuFullLoadExecutor());
    }

    @Override
    public String getName() {
        return JvmConstant.ACTION_CPU_FULL_LOAD_NAME;
    }

    @Override
    public String[] getAliases() {
        return new String[] {JvmConstant.ACTION_CPU_FULL_LOAD_ALIAS};
    }

    @Override
    public String getShortDesc() {
        return "Process occupied cpu full load";
    }

    @Override
    public String getLongDesc() {
        return "Process occupied cpu full load";
    }

    @Override
    public List<FlagSpec> getActionFlags() {
        ArrayList<FlagSpec> flagSpecs = new ArrayList<FlagSpec>();
        flagSpecs.add(new CpuCountFlagSpec());
        return flagSpecs;
    }

    @Override
    public PredicateResult predicate(ActionModel actionModel) {
        return PredicateResult.success();
    }

    @Override
    public void createInjection(String uid, Model model) throws Exception {
        EnhancerModel enhancerModel = new EnhancerModel(EnhancerModel.class.getClassLoader(), model.getMatcher());
        enhancerModel.merge(model);
        getActionExecutor().run(enhancerModel);
    }

    @Override
    public void destroyInjection(String uid, Model model) throws Exception {
        EnhancerModel enhancerModel = new EnhancerModel(EnhancerModel.class.getClassLoader(), model.getMatcher());
        enhancerModel.merge(model);
        ((StoppableActionExecutor)getActionExecutor()).stop(enhancerModel);
    }

    @Override
    public String getExample() {
        return "# Specifies full load of all kernel\n" +
            "blade c jvm cfl --process tomcat\n\n" +

            "# Specifies full load of two kernel\n" +
            "blade c jvm cfl --cpu-count 2 --process tomcat";
    }

    @Override
    public String[] getCategories() {
        return new String[] {CategoryConstants.JAVA_RESOURCE_CPU};
    }
}
