package com.alibaba.chaosblade.exec.plugin.jvm.oom;

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
import com.alibaba.chaosblade.exec.common.util.StringUtils;
import com.alibaba.chaosblade.exec.plugin.jvm.JvmConstant;
import com.alibaba.chaosblade.exec.plugin.jvm.StoppableActionExecutor;
import com.alibaba.chaosblade.exec.plugin.jvm.oom.executor.JvmOomExecutorFacade;
import com.alibaba.chaosblade.exec.plugin.jvm.oom.flag.JvmMemoryAreaFlagSpec;
import com.alibaba.chaosblade.exec.plugin.jvm.oom.flag.OOMMemeoryErrorIntervalFlagSpec;
import com.alibaba.chaosblade.exec.plugin.jvm.oom.flag.OOMMemoryBlockFlagSpec;
import com.alibaba.chaosblade.exec.plugin.jvm.oom.flag.OOMModeFlagSpec;

/**
 * @author RinaisSuper
 * @date 2019-04-18
 * @email rinalhb@icloud.com
 */
public class JvmOomActionSpec extends BaseActionSpec implements DirectlyInjectionAction {

    public JvmOomActionSpec() {
        super(new JvmOomExecutorFacade());
    }

    @Override
    public String getName() {
        return JvmConstant.ACTION_OUT_OF_MEMORY_ERROR_NAME;
    }

    @Override
    public String[] getAliases() {
        return new String[]{"oom", "outofmemoryerror"};
    }

    @Override
    public String getShortDesc() {
        return "JVM out of memory";
    }

    @Override
    public String getLongDesc() {
        return "JVM out of memory";
    }

    @Override
    public List<FlagSpec> getActionFlags() {
        List<FlagSpec> flagSpecs = new ArrayList<FlagSpec>();
        flagSpecs.add(new JvmMemoryAreaFlagSpec());
        flagSpecs.add(new OOMModeFlagSpec());
        flagSpecs.add(new OOMMemeoryErrorIntervalFlagSpec());
        flagSpecs.add(new OOMMemoryBlockFlagSpec());
        return flagSpecs;
    }

    @Override
    public PredicateResult predicate(ActionModel actionModel) {
        String area = actionModel.getFlag(JvmConstant.FLAG_NAME_MEMORY_AREA);
        if (StringUtils.isEmpty(area)) {
            return PredicateResult.fail("missed argument:area");
        }
        JvmMemoryArea jvmMemoryArea = JvmMemoryArea.nameOf(area);
        if (jvmMemoryArea == null) {
            return PredicateResult.fail("illegal argument value:area [" + area + "]");
        }
        return PredicateResult.success();
    }

    @Override
    public void createInjection(String expId, Model model) throws Exception {
        EnhancerModel enhancerModel = new EnhancerModel(EnhancerModel.class.getClassLoader(), model.getMatcher());
        enhancerModel.merge(model);
        getActionExecutor().run(enhancerModel);
    }

    @Override
    public void destroyInjection(String expId, Model model) throws Exception {
        EnhancerModel enhancerModel = new EnhancerModel(EnhancerModel.class.getClassLoader(), model.getMatcher());
        enhancerModel.merge(model);
        ((StoppableActionExecutor) getActionExecutor()).stop(enhancerModel);
    }

    @Override
    public String getExample() {
        return "# The Heap area is filled with memory.\n" +
                "blade c jvm oom --area HEAP --wild-mode true\n\n" +

                "# The Metaspace area is filled with memory. Note that after executing this experiment, the application needs to be restarted ！！！\n" +
                "blade c jvm oom --area NOHEAP --wild-mode true";

    }

    @Override
    public String[] getCategories() {
        return new String[]{CategoryConstants.JAVA_RESOURCE_CPU};
    }
}
