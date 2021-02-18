package com.alibaba.chaosblade.exec.plugin.jvm.thread.model;

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
import com.alibaba.chaosblade.exec.plugin.jvm.thread.JvmThreadPoolFullExecutor;

/**
 * @Author Yuhan Tang
 * @package: com.alibaba.chaosblade.exec.plugin.jvm.thread
 * @Date 2020-11-02 13:38
 */
public class JvmThreadFullActionSpec extends BaseActionSpec implements DirectlyInjectionAction {

    public JvmThreadFullActionSpec() {
        super(new JvmThreadPoolFullExecutor());
    }

    @Override
    public String getName() {
        return JvmConstant.THREAD_FULL;
    }

    @Override
    public String[] getAliases() {
        return new String[] {JvmConstant.ACTION_THREAD_FULL_ALIAS};
    }

    @Override
    public String getShortDesc() {
        return "Specifies that the application thread is soaring";
    }

    @Override
    public String getLongDesc() {
        return "Specifies that the application thread is soaring";
    }

    @Override
    public List<FlagSpec> getActionFlags() {
        ArrayList<FlagSpec> flagSpecs = new ArrayList<FlagSpec>();
        flagSpecs.add(new JvmThreadCountSpec());
        flagSpecs.add(new ThreadRunningMatcherSpec());
        flagSpecs.add(new ThreadWaitMatcherSpec());
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
        return "# Specifies that the application thread is soaring and the status wait\n" +
            "blade create jvm threadfull --wait --thread-count 20\n\n" +

            "#Specifies that the application thread is soaring and running status\n" +
            "blade create jvm threadfull --running --thread-count 20";
    }

    @Override
    public String[] getCategories() {
        return new String[] {CategoryConstants.JAVA_RESOURCE_CPU};
    }
}
