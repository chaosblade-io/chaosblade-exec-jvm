package com.alibaba.chaosblade.exec.plugin.jvm.thread;

import com.alibaba.chaosblade.exec.common.aop.EnhancerModel;
import com.alibaba.chaosblade.exec.common.aop.PredicateResult;
import com.alibaba.chaosblade.exec.common.model.FlagSpec;
import com.alibaba.chaosblade.exec.common.model.Model;
import com.alibaba.chaosblade.exec.common.model.action.ActionModel;
import com.alibaba.chaosblade.exec.common.model.action.BaseActionSpec;
import com.alibaba.chaosblade.exec.common.model.action.DirectlyInjectionAction;
import com.alibaba.chaosblade.exec.plugin.jvm.JvmConstant;
import com.alibaba.chaosblade.exec.plugin.jvm.StoppableActionExecutor;

import java.util.ArrayList;
import java.util.List;

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
        return new String[]{JvmConstant.ACTION_THREAD_FULL_ALIAS};
    }

    @Override
    public String getShortDesc() {
        return "Process thread full load";
    }

    @Override
    public String getLongDesc() {
        return "Process thread full load";
    }

    @Override
    public List<FlagSpec> getActionFlags() {
        ArrayList<FlagSpec> flagSpecs = new ArrayList<FlagSpec>();
        flagSpecs.add(new JvmThreadCountSpec());
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
        ((StoppableActionExecutor) getActionExecutor()).stop(enhancerModel);
    }

    @Override
    public String getExample() {
        return "# Specifies full load of all kernel\n" +
                "blade c jvm thread --process tomcat\n\n" +

                "# Specifies full load of two kernel\n" +
                "blade c jvm thread --thread-count 2 --process tomcat";
    }

}
