package com.alibaba.chaosblade.exec.common.model.handler;

import com.alibaba.chaosblade.exec.common.exception.ExperimentException;
import com.alibaba.chaosblade.exec.common.model.Model;

/**
 * Model Level Handler before create injections
 *
 * <p>Useful for  need do something on model level not action level when receive a create request </p>
 * <p>
 * For example,a module contains multiple actions, before invoke actions,we need do something,so you can make @{@link
 * com.alibaba.chaosblade.exec.common.model.ModelSpec} implements this interface.
 * </p>
 *
 * @author haibin
 * @date 2019-04-19
 */
public interface PreCreateInjectionModelHandler {

    /**
     * Invoke before create injections
     *
     * @param suid  the injection event id
     * @param model model object
     * @throws ExperimentException throw if anything wrong
     */
    public void preCreate(String suid, Model model) throws ExperimentException;
}
