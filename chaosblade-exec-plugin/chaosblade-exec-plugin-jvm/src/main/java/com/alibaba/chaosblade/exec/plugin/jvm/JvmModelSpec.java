package com.alibaba.chaosblade.exec.plugin.jvm;

import com.alibaba.chaosblade.exec.common.exception.ExperimentException;
import com.alibaba.chaosblade.exec.common.model.Model;
import com.alibaba.chaosblade.exec.common.model.action.ActionSpec;
import com.alibaba.chaosblade.exec.common.model.action.DirectlyInjectionAction;
import com.alibaba.chaosblade.exec.common.model.handler.PreCreateInjectionModelHandler;
import com.alibaba.chaosblade.exec.common.model.handler.PreDestroyInjectionModelHandler;
import com.alibaba.chaosblade.exec.common.plugin.MethodModelSpec;
import com.alibaba.chaosblade.exec.plugin.jvm.oom.JvmOomActionSpec;

/**
 * Jvm model spec
 * <p>
 * The jvm model plugin is used  for creating common java fault injections.
 * </p>
 * <p>
 * For example
 * <ul>
 * <li>inject any java methods</li>
 * <li>cause jvm oom</li>
 * </ul>
 * </p>
 *
 * @author haibin
 * @date 2019-04-19
 */
public class JvmModelSpec extends MethodModelSpec implements PreCreateInjectionModelHandler,
    PreDestroyInjectionModelHandler {

    public JvmModelSpec() {
        super();
        addActionSpec(new JvmOomActionSpec());
    }

    @Override
    public void preDestroy(String uid, Model model) throws ExperimentException {
        ActionSpec actionSpec = getActionSpec(model.getActionName());
        if (actionSpec instanceof DirectlyInjectionAction) {
            try {
                ((DirectlyInjectionAction)actionSpec).destroyInjection(uid, model);
            } catch (Exception e) {
                throw new ExperimentException("destroy injection failed:" + e.getMessage());
            }
        } else {
            MethodPreInjectHandler.preHandleRecovery(model);
        }
    }

    @Override
    public void preCreate(String uid, Model model) throws ExperimentException {
        ActionSpec actionSpec = getActionSpec(model.getActionName());
        if (actionSpec instanceof DirectlyInjectionAction) {
            try {
                ((DirectlyInjectionAction)actionSpec).createInjection(uid, model);
            } catch (Exception e) {
                throw new ExperimentException("create injection failed:" + e.getMessage());
            }
        } else {
            MethodPreInjectHandler.preHandleInjection(model);
        }
    }
}
