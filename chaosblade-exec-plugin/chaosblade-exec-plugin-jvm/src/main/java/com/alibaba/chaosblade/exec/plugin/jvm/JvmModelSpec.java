package com.alibaba.chaosblade.exec.plugin.jvm;

import com.alibaba.chaosblade.exec.common.exception.ExperimentException;
import com.alibaba.chaosblade.exec.common.model.Model;
import com.alibaba.chaosblade.exec.common.model.handler.PreCreateInjectionModelHandler;
import com.alibaba.chaosblade.exec.common.model.handler.PreDestroyInjectionModelHandler;
import com.alibaba.chaosblade.exec.common.plugin.MethodModelSpec;

/**
 * Jvm model spec
 * <p>
 * The jvm model plugin is used  for creating common java fault injections.
 * </p>
 * <p>
 * For example
 * <ul>
 *  <li>inject any java methods</li>
 *  <li>cause jvm oom</li>
 * </ul>
 * </p>
 *
 * @author haibin
 * @date 2019-04-19
 */
public class JvmModelSpec extends MethodModelSpec implements PreCreateInjectionModelHandler,
    PreDestroyInjectionModelHandler {

    @Override
    public void preDestroy(Model model) throws ExperimentException {
        MethodPreInjectHandler.preHandleRecovery(model);
    }

    @Override
    public void preCreate(Model model) throws ExperimentException {
        MethodPreInjectHandler.preHandleInjection(model);
    }
}
