package com.alibaba.chaosblade.exec.common.model.handler;

import com.alibaba.chaosblade.exec.common.exception.ExperimentException;
import com.alibaba.chaosblade.exec.common.model.Model;

/**
 * Model Level Handler before destroy injections
 *
 * <p>Useful for  need do something on model level not action level when receive a destroy request </p>
 * <p>
 * For example,a module contains multiple actions, before invoke destroy actions,we need do something,so you can make
 *
 * @author RinaisSuper
 * @{@link com.alibaba.chaosblade.exec.common.model.ModelSpec} implements this interface.
 * </p>
 * @date 2019-04-19
 */
public interface PreDestroyInjectionModelHandler {

    /**
     * Invoke before destroy injections
     *
     * @param suid  the injection event id
     * @param model the model
     * @throws ExperimentException throw if anything wrong
     */
    void preDestroy(String suid, Model model) throws ExperimentException;
}
